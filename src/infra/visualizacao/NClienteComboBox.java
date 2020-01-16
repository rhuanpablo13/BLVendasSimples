/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NCliente;

/**
 *
 * @author rhuan
 */
public class NClienteComboBox extends DefaultComboBoxModel {

    
    private List<NCliente> list;
    private NCliente selectedCliente;
    private final static int FIRST_INDEX = 0;

    public NClienteComboBox(List<NCliente> list) {
        this.list = list;
    }

    public NClienteComboBox() {
        this.list = new ArrayList();
    }
    
    
    
    @Override
    public int getSize() {
        return list.size();        
    }

    @Override
    public NCliente getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedCliente = (NCliente) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedCliente;
    }
    
    
    
    public void addListCliente(List<NCliente> list) {
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
        NCliente last = list.get(list.size()-1);
        setSelectedItem(last);
    }
    
    public void updateForCodigo(int codigo) {
        for (NCliente cliente : list) {
            if (cliente.getCodigo() == codigo) {
                setSelectedItem(cliente);
            }
        }
    }
    
    public void updateForNome(String nome) {
        List<NCliente> lista = new ArrayList();
        for (NCliente cliente : list) {
            if (cliente.getNome().equalsIgnoreCase(nome)) {
                lista.add(cliente);
            }
        }
        addListCliente(list);
    }    
}
