/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NFornecedor;

/**
 *
 * @author rhuan
 */
public class NFornecedorComboBox extends DefaultComboBoxModel <NFornecedor> {

    private List<NFornecedor> list;
    private NFornecedor selectedFornecedor;
    private final static int FIRST_INDEX = 0;
    
    
    public NFornecedorComboBox() {
        this.list = new ArrayList();
    }

    
    public NFornecedorComboBox(List<NFornecedor> list) {
        this.list = list;
    }
    
    
    
    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public NFornecedor getElementAt(int index) {
        return list.get(index);
    }
    
    @Override
    public void setSelectedItem(Object anItem) {
        if ((selectedFornecedor != null && !selectedFornecedor.equals( (NFornecedor) anItem )) ||
            selectedFornecedor == null && anItem != null) {
            selectedFornecedor = (NFornecedor) anItem;
            fireContentsChanged(this, -1, -1);
            System.out.println("fire");
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedFornecedor;
    }

    @Override
    public void addElement(NFornecedor anObject) {        
        NFornecedor fornecedor = anObject;
        if (!list.contains(fornecedor)) {
            list.add(fornecedor);            
        }
        fireIntervalAdded(fornecedor, list.size()-1, list.size()-1);
        if (list.size() == 1 && selectedFornecedor == null && fornecedor != null) {
            setSelectedItem(fornecedor);
        }
    }

    public void setSelectedLastElement() {
        if (list.isEmpty()) return;
        NFornecedor last = list.get(list.size()-1);
        setSelectedItem(last);
    }
    
    public void addListFornecedor(List<NFornecedor> list) {
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

    
    public void updateForCodigo(int codigo) {
        for (NFornecedor fornecedor : list) {
            if (fornecedor.getCodigo() == codigo) {
                setSelectedItem(fornecedor);
            }
        }
    }
}