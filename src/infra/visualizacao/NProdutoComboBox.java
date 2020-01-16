/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class NProdutoComboBox extends DefaultComboBoxModel <NProduto> {

    
    private List<NProduto> list;
    private NProduto selectedProduto;
    private final static int FIRST_INDEX = 0;

    public NProdutoComboBox(List<NProduto> list) {
        this.list = list;
    }

    public NProdutoComboBox() {
        list = new ArrayList();
    }
    
    
    @Override
    public int getSize() {
        return list.size();        
    }

    @Override
    public NProduto getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedProduto = (NProduto) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedProduto;
    }

    @Override
    public void removeAllElements() {
        list = new ArrayList();
        fireIntervalRemoved(this, FIRST_INDEX, FIRST_INDEX);
    }
    
    
    
    
    public void addListProduto(List<NProduto> list) {
        if (!list.isEmpty()) {
            int primeiraLinha = getSize();
            this.list.addAll(list);
            fireIntervalAdded(this, primeiraLinha, primeiraLinha  + this.list.size());
            setSelectedItem(this.list.get(getSize() - 1));            
        }
    }
    
    
    public void remove() {
        boolean remove = this.list.remove(getSelectedItem());
        if (remove) {
            fireIntervalRemoved(this, FIRST_INDEX, getSize() - 1);
            setSelectedItem(this.list.get(FIRST_INDEX));
        }
    }
    
    public void clear() {
        this.list = new ArrayList();
        fireContentsChanged(this, FIRST_INDEX, getSize() - 1);
    }

    public void setSelectedLastElement() {
        if (list.size() > 0) {
            NProduto last = list.get(list.size()-1);
            setSelectedItem(last);
        }
    }
    
    public void updateForCodigo(int codigo) {
        for (NProduto cliente : list) {
            if (cliente.getCodigo() == codigo) {
                setSelectedItem(cliente);
            }
        }
    }
    
    public void updateForNome(String nome) {
        for (NProduto cliente : list) {
            if (cliente.getNome().equalsIgnoreCase(nome)) {
                setSelectedItem(cliente);
            }
        }
    }
    
    public List<NProduto> getAllItems() {
        return list;
    }
}
