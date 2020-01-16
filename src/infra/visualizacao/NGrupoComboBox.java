/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NGrupo;

/**
 *
 * @author rhuan
 */
public class NGrupoComboBox extends DefaultComboBoxModel <NGrupo>  {

    private List<NGrupo> list;
    private NGrupo selectedGrupo;
    private final static int FIRST_INDEX = 0;
    
    
    public NGrupoComboBox() {
        this.list = new ArrayList();
    }
    
    
    public NGrupoComboBox(List<NGrupo> list) {
        this.list = list;
    }

    
    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public NGrupo getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if ((selectedGrupo != null && !selectedGrupo.equals( (NGrupo) anItem )) ||
            selectedGrupo == null && anItem != null) {
            selectedGrupo = (NGrupo) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedGrupo;
    }

    @Override
    public void addElement(NGrupo anObject) {
        NGrupo grupo = (NGrupo) anObject;
        if (!list.contains(grupo)) {
            list.add(grupo);            
        }
        fireIntervalAdded(grupo, list.size()-1, list.size()-1);
        if (list.size() == 1 && selectedGrupo == null && grupo != null) {
            setSelectedItem(grupo);
        }
    }
    
    
    public void addListGrupo(List<NGrupo> list) {
        int primeiraLinha = getSize();
        this.list.addAll(list);
        fireIntervalAdded(this, primeiraLinha, primeiraLinha  + this.list.size());
        setSelectedItem(this.list.get(getSize() - 1));
    }
    
    
    public void remove() {
        boolean remove = this.list.remove(getSelectedItem());
        if (remove) {
            fireIntervalRemoved(this, FIRST_INDEX, getSize() - 1);
            setSelectedItem(this.list.get(FIRST_INDEX));
        }
    }
    
    public void clear() {
        this.list.clear();
        fireContentsChanged(this, FIRST_INDEX, getSize() - 1);
    }
    
    public void updateForCodigo(int codigo) {
        for (NGrupo grupo : list) {
            if (grupo.getCodigo() == codigo) {
                setSelectedItem(grupo);
            }
        }
    }
    
    
    public void setSelectedLastElement() {
        if (list.isEmpty()) return;
        NGrupo last = list.get(list.size()-1);
        setSelectedItem(last);
    }
    
}
