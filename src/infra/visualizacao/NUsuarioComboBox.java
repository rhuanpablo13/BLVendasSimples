/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import sistemavendas.negocio.NUsuario;

/**
 *
 * @author rhuan
 */
public class NUsuarioComboBox  extends DefaultComboBoxModel {

    
    private List<NUsuario> list;
    private NUsuario selectedUsuario;
    private final static int FIRST_INDEX = 0;

    public NUsuarioComboBox(List<NUsuario> list) {
        this.list = list;
    }

    public NUsuarioComboBox() {
        this.list = new ArrayList();
    }
    
    
    
    @Override
    public int getSize() {
        return list.size();        
    }

    @Override
    public NUsuario getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedUsuario = (NUsuario) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedUsuario;
    }
    
    
    
    public void addListUsuario(List<NUsuario> list) {
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
        NUsuario last = list.get(list.size()-1);
        setSelectedItem(last);
    }
    
    public void updateForId(int id) {
        for (NUsuario usuario : list) {
            if (usuario.getId() == id) {
                setSelectedItem(usuario);
            }
        }
    }
    
    public void updateForNome(String nome) {
        List<NUsuario> lista = new ArrayList();
        for (NUsuario usuario : list) {
            if (usuario.getNome().equalsIgnoreCase(nome)) {
                lista.add(usuario);
            }
        }
        addListUsuario(list);
    }    
    
}
