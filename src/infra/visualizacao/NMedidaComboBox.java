/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NMedida;

/**
 *
 * @author rhuan
 */
public class NMedidaComboBox extends DefaultComboBoxModel {
    
    
    private List<NMedida> list;
    private NMedida selectedMedida;
    private final static int FIRST_INDEX = 0;

    public NMedidaComboBox() {
    }

    public NMedidaComboBox(List<NMedida> list) {
        this.list = list;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedMedida = (NMedida) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedMedida;
    }

    @Override
    public void addElement(Object anObject) {
        NMedida Medida = (NMedida) anObject;
        if (!list.contains(Medida)) {
            list.add(Medida);            
        }
        fireIntervalAdded(Medida, list.size()-1, list.size()-1);
        if (list.size() == 1 && selectedMedida == null && Medida != null) {
            setSelectedItem(Medida);
        }
    }
    
    
    public void setSelectedLastElement() {
        if (list.isEmpty()) return;
        NMedida last = list.get(list.size()-1);
        setSelectedItem(last);
    }
    
    
    public void addListMedida(List<NMedida> list) {
        
        if (!this.list.isEmpty()) {
            this.list.clear();
        }
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
}
