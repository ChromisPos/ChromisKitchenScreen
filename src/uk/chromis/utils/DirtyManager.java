/*
 Chromis POS  - The New Face of Open Source POS
 Copyright (c) 2015 (John Lewis) Chromis.co.uk

 http://www.chromis.co.uk

 kitchen Screen v1.5

 This file is part of chromis & its associated programs

 chromis is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 chromis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with chromis.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.chromis.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class DirtyManager implements ChangeListener, PropertyChangeListener {

    private Boolean m_dirty;

    public DirtyManager() {
        m_dirty = false;
    }

    public Boolean isDirty() {
        return m_dirty;
    }

    public void setDirty(){
        m_dirty=true;
    }
    
    public void resetDirty(){
        m_dirty=false;
    }
      
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        m_dirty = true;
    }
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        m_dirty = true;
    }


}
