package com.example.gamificationapp.repository;

import com.example.gamificationapp.exceptions.RepoException;

public interface IRepository <T,ID>{
    void add(T entity) throws RepoException;
    void remove(ID id) throws RepoException;
    T findElement(ID id) throws RepoException;
    Iterable<T> getAll();
}
