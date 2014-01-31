/*
 * Copyright 2013 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.collection.mutable;

import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.AbstractRichIterableTestCase;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * Abstract JUnit test for {@link MutableCollection}s.
 */
public abstract class AbstractCollectionTestCase extends AbstractRichIterableTestCase
{
    @Override
    protected abstract <T> MutableCollection<T> classUnderTest();

    @Test
    public void newEmpty()
    {
        MutableCollection<Object> collection = this.classUnderTest().newEmpty();
        Verify.assertEmpty(collection);
        Verify.assertSize(0, collection);
        Assert.assertFalse(collection.notEmpty());
    }

    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(MutableCollection.class, this.<Object>classUnderTest());
        Verify.assertInstanceOf(ImmutableCollection.class, this.classUnderTest().toImmutable());
    }

    @Override
    protected <T> MutableCollection<T> newWith(T one)
    {
        MutableCollection<T> result = this.classUnderTest();
        result.add(one);
        return result;
    }

    @Override
    protected <T> MutableCollection<T> newWith(T one, T two)
    {
        MutableCollection<T> result = this.classUnderTest();
        result.add(one);
        result.add(two);
        return result;
    }

    @Override
    protected <T> MutableCollection<T> newWith(T one, T two, T three)
    {
        MutableCollection<T> result = this.classUnderTest();
        result.add(one);
        result.add(two);
        result.add(three);
        return result;
    }

    @Override
    protected <T> MutableCollection<T> newWith(T... littleElements)
    {
        MutableCollection<T> result = this.classUnderTest();
        for (int i = 0; i < littleElements.length; i++)
        {
            result.add(littleElements[i]);
        }
        return result;
    }

    @Test
    public void testNewWith()
    {
        MutableCollection<Integer> collection = this.newWith(1);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(1, collection);
        Verify.assertContains(1, collection);
    }

    @Test
    public void testNewWithWith()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(2, collection);
        Verify.assertContainsAll(collection, 1, 2);
    }

    @Test
    public void testNewWithWithWith()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(3, collection);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void testNewWithVarArgs()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(4, collection);
        Verify.assertContainsAll(collection, 1, 2, 3, 4);
    }

    @Test
    public void addAll()
    {
        MutableCollection<Integer> collection = this.classUnderTest();
        Assert.assertTrue(collection.addAll(FastList.newListWith(1, 2, 3)));
        Verify.assertContainsAll(collection, 1, 2, 3);

        boolean result = collection.addAll(FastList.newListWith(1, 2, 3));
        if (collection.size() == 3)
        {
            Assert.assertFalse("addAll did not modify the collection", result);
        }
        else
        {
            Assert.assertTrue("addAll modified the collection", result);
        }
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void addAllIterable()
    {
        MutableCollection<Integer> collection = this.classUnderTest();
        Assert.assertTrue(collection.addAllIterable(FastList.newListWith(1, 2, 3)));
        Verify.assertContainsAll(collection, 1, 2, 3);

        boolean result = collection.addAllIterable(FastList.newListWith(1, 2, 3));
        if (collection.size() == 3)
        {
            Assert.assertFalse("addAllIterable did not modify the collection", result);
        }
        else
        {
            Assert.assertTrue("addAllIterable modified the collection", result);
        }
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void removeAll()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.removeAll(FastList.newListWith(1, 2, 4)));
        Assert.assertEquals(Bags.mutable.of(3), objects.toBag());

        MutableCollection<Integer> objects2 = this.newWith(1, 2, 3);
        Assert.assertFalse(objects2.removeAll(FastList.newListWith(4, 5)));
        Assert.assertEquals(Bags.mutable.of(1, 2, 3), objects2.toBag());
    }

    @Test
    public void removeAllIterable()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.removeAllIterable(FastList.newListWith(1, 2, 4)));
        Assert.assertEquals(Bags.mutable.of(3), objects.toBag());

        MutableCollection<Integer> objects2 = this.newWith(1, 2, 3);
        Assert.assertFalse(objects2.removeAllIterable(FastList.newListWith(4, 5)));
        Assert.assertEquals(Bags.mutable.of(1, 2, 3), objects2.toBag());
    }

    @Test
    public void retainAll()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.retainAll(mSet(1, 2)));
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 1, 2);

        MutableCollection<Integer> integers1 = this.newWith(0);
        Assert.assertFalse(integers1.retainAll(FastList.newListWith(1, 0)));
        Assert.assertEquals(Bags.mutable.of(0), integers1.toBag());

        MutableCollection<Integer> integers2 = this.newWith(1, 2, 3);
        Integer copy = new Integer(1);
        Assert.assertTrue(integers2.retainAll(FastList.newListWith(copy)));
        Assert.assertEquals(Bags.mutable.of(1), integers2.toBag());
        Assert.assertNotSame(copy, integers2.getFirst());
    }

    @Test
    public void retainAllIterable()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.retainAllIterable(iList(1, 2)));
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 1, 2);

        MutableCollection<Integer> integers = this.newWith(0);
        Assert.assertFalse(integers.retainAllIterable(FastList.newListWith(1, 0)));
        Assert.assertEquals(Bags.mutable.of(0), integers.toBag());
    }

    @Test
    public void clear()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        objects.clear();
        Verify.assertSize(0, objects);
        Verify.assertEmpty(objects);
        objects.clear();
        Verify.assertEmpty(objects);
    }

    @Test
    public void injectIntoWith()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Integer result = objects.injectIntoWith(1, new Function3<Integer, Integer, Integer, Integer>()
        {
            public Integer value(Integer injectedValued, Integer item, Integer parameter)
            {
                return injectedValued + item + parameter;
            }
        }, 0);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void removeObject()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        objects.remove(3);
        Verify.assertSize(2, objects);
    }

    @Test
    public void selectAndRejectWith()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2);
        Twin<MutableList<Integer>> result = objects.selectAndRejectWith(Predicates2.equal(), 1);
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(1, result.getTwo());
    }

    @Test
    public void remove()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        objects.add(null);
        objects.removeIf(Predicates.isNull());
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
    }

    @Test
    public void removeIfWith()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3, 4);
        objects.removeIfWith(Predicates2.<Integer>lessThan(), 3);
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 3, 4);
    }

    @Test
    public void with()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWith = coll.with(4);
        Assert.assertSame(coll, collWith);
        Assert.assertEquals(this.newWith(1, 2, 3, 4), collWith);
    }

    @Test
    public void withAll()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWith = coll.withAll(FastList.newListWith(4, 5));
        Assert.assertSame(coll, collWith);
        Assert.assertEquals(this.newWith(1, 2, 3, 4, 5), collWith);
    }

    @Test
    public void without()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWithout = coll.without(2);
        Assert.assertSame(coll, collWithout);
        Assert.assertEquals(this.newWith(1, 3), collWithout);
    }

    @Test
    public void withoutAll()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3, 4, 5);
        MutableCollection<Integer> collWithout = coll.withoutAll(FastList.newListWith(2, 4));
        Assert.assertSame(coll, collWithout);
        Assert.assertEquals(this.newWith(1, 3, 5), collWithout);
    }

    @Test
    public abstract void asSynchronized();

    @Test
    public abstract void asUnmodifiable();
}
