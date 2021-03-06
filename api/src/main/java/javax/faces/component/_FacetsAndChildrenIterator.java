/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.faces.component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _FacetsAndChildrenIterator
        implements Iterator
{
    private Iterator _facetsIterator;
    private Iterator _childrenIterator;

    _FacetsAndChildrenIterator(Map facetMap, List childrenList)
    {
        _facetsIterator   = facetMap != null ? facetMap.values().iterator() : null;
        _childrenIterator = childrenList != null ? childrenList.iterator() : null;
    }

    public boolean hasNext()
    {
        return (_facetsIterator != null && _facetsIterator.hasNext()) ||
               (_childrenIterator != null && _childrenIterator.hasNext());
    }

    public Object next()
    {
        if (_facetsIterator != null && _facetsIterator.hasNext())
        {
            return _facetsIterator.next();
        }
        else if (_childrenIterator != null && _childrenIterator.hasNext())
        {
            return _childrenIterator.next();
        }
        else
        {
            throw new NoSuchElementException();
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
    }

}
