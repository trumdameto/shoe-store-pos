/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.List;

/**
 *
 * @author HÙNG
 */
public abstract class BaseCRUD<T> {

    abstract public Integer insert(T entity);

    abstract public Integer update(T entity);

    abstract public Integer delete(T entity);

    abstract public T selectbyId(T entity);

    abstract public List<T> selectAll();

}
