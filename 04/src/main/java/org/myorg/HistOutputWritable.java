package org.myorg;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by oleg on 10/12/16.
 */
public class HistOutputWritable implements Writable {
    private long[] data;

    public HistOutputWritable(long[] data) {
        this.data = data;
    }

    public void write(DataOutput dataOutput) throws IOException {
        int length = 0;
        if(data != null) {
            length = data.length;
        }

        dataOutput.writeInt(length);

        for(int i = 0; i < length; i++) {
            dataOutput.writeLong(data[i]);
        }
    }

    public void readFields(DataInput dataInput) throws IOException {
        int length = dataInput.readInt();

        data = new long[length];

        for(int i = 0; i < length; i++) {
            data[i] = dataInput.readLong();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (data.length == 0) {
            return "";
        }
        sb.append(data[0]);
        for (int i = 1; i < data.length; i++) {
            sb.append(" ");
            sb.append(data[i]);
        }
        return sb.toString();
    }
}
