/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NPrograma;

/**
 *
 * @author rhuan
 */
public class NProgramaComboBox extends DefaultComboBoxModel {

    
    private List<NPrograma> list;
    private NPrograma selectedPrograma;
    private final static int FIRST_INDEX = 0;

    public NProgramaComboBox(List<NPrograma> list) {
        this.list = list;
    }

    public NProgramaComboBox() {
        this.list = new ArrayList();
    }
    
    
    
    @Override
    public int getSize() {
        return list.size();        
    }

    @Override
    public NPrograma getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedPrograma = (NPrograma) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedPrograma;
    }
    
    
    
    public void addListPrograma(List<NPrograma> list) {
        this.list = new ArrayList();
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

    public void setSelectedLastElement() {
        if (list.isEmpty()) return;
        NPrograma last = list.get(list.size()-1);
        setSelectedItem(last);
    }
    
    public void updateForId(int id) {
        for (NPrograma programa : list) {
            if (programa.getId() == id) {
                setSelectedItem(programa);
            }
        }
    }
    
    public void updateForNome(String nome) {
        List<NPrograma> lista = new ArrayList();
        for (NPrograma programa : list) {
            if (programa.getNome().equalsIgnoreCase(nome)) {
                lista.add(programa);
            }
        }
        addListPrograma(list);
    }    
    
}
