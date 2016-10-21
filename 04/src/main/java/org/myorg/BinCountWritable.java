package org.myorg;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by oleg on 10/12/16.
 */
public class BinCountWritable implements Writable {
    public int bin;
    public long count;

    public BinCountWritable() {

    }

    public BinCountWritable(int bin, long count) {
        this.bin = bin;
        this.count = count;
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(bin);
        dataOutput.writeLong(count);
    }

    public void readFields(DataInput dataInput) throws IOException {
        bin = dataInput.readInt();
        count = dataInput.readLong();
    }
}
