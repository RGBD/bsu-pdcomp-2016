package org.myorg;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.codehaus.jackson.map.DeserializerFactory;

public class Histogram {

    public static class Map extends Mapper<LongWritable, Text, Text, BinCountWritable> {
        private Text word = new Text();
        private static long nBins;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            nBins = context.getConfiguration().getInt("n_bins", 1);
        }

        public void map(LongWritable _, Text composite, Context context) throws IOException, InterruptedException {
            String[] split = composite.toString().split("\t");
            String key = split[0];
            double value = Double.parseDouble(split[1]);
            if (value < 0 || value >= 1) {
                return;
            }
            int bin = (int)(value * nBins);
            word.set(key);
            context.write(word, new BinCountWritable(bin, 1));
        }
    }

    public static class Combine extends Reducer<Text, BinCountWritable, Text, BinCountWritable> {
        private int nBins;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            nBins = context.getConfiguration().getInt("n_bins", 1);
        }

        public void reduce(Text key, Iterable<BinCountWritable> values, Context context)
                throws IOException, InterruptedException {
            long counts[] = new long[nBins];
            for (BinCountWritable val : values) {
                counts[val.bin] += val.count;
            }
            for (int i = 0; i < counts.length; i++) {
                context.write(key, new BinCountWritable(i, counts[i]));
            }
        }
    }

    public static class Reduce extends Reducer<Text, BinCountWritable, Text, HistOutputWritable> {
        private int nBins;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            nBins = context.getConfiguration().getInt("n_bins", 1);
        }

        public void reduce(Text key, Iterable<BinCountWritable> values, Context context)
                throws IOException, InterruptedException {
            long counts[] = new long[nBins];
            for (BinCountWritable val : values) {
                counts[val.bin] += val.count;
            }
            context.write(key, new HistOutputWritable(counts));
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            return;
        }

        Configuration conf = new Configuration();
        conf.setInt("n_bins", Integer.parseInt(args[2]));

        Job job = Job.getInstance(conf, "histogram");

        job.setMapperClass(Map.class);
        job.setCombinerClass(Combine.class);
        job.setReducerClass(Reduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BinCountWritable.class);
        job.setOutputValueClass(Text.class);
        job.setOutputValueClass(HistOutputWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileSystem fs = FileSystem.get(new Configuration());
        fs.delete(new Path(args[1]), true);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }

}

