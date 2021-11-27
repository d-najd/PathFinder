package com.app.Objects;

import java.util.ArrayList;

public class QueuePiece {
    private int x = 0;
    private int y = 0;
    private ArrayList<QueuePiece> visitedPieces = new ArrayList<>();

    public QueuePiece(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void AddParent(ArrayList<QueuePiece> previousPath, QueuePiece visited){
        //I am not sure how or why this works but hey it prevents a memory leak and crisis prevented
        visitedPieces = previousPath;
        visitedPieces.add(new QueuePiece(visited.getX(), visited.getY()));
    }

    public ArrayList<QueuePiece> getPath(){
        return visitedPieces;
    }

    public String getPathString(){
        StringBuilder builder = new StringBuilder();

        for (QueuePiece piece : visitedPieces){
            builder.append(" Piece ").append(piece.getX()).append(" ").append(piece.getY());
        }

        return builder.toString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}