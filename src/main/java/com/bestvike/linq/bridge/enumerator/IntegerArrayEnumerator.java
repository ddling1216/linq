package com.bestvike.linq.bridge.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class IntegerArrayEnumerator extends AbstractEnumerator<Integer> {
    private final int[] source;

    public IntegerArrayEnumerator(int[] source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;
        if (this.state < this.source.length) {
            this.current = this.source[this.state++];
            return true;
        }
        this.close();
        return false;
    }
}
