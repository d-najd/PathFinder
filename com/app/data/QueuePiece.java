package com.app.data;

import java.util.ArrayList;
import java.util.List;

public class QueuePiece extends Piece {
    private List<QueuePiece> visitedPieces = new ArrayList<>();

    public QueuePiece(int x, int y) {
        super(x, y);
    }

    public QueuePiece(int x, int y, Piece.Type startType) {
        super(x, y);
        this.type = startType;
        this.startType = startType;
    }

    public void addParent(QueuePiece previousPiece, QueuePiece visited){
        visitedPieces = new ArrayList<>(previousPiece.getPath());
        visitedPieces.add(visited);
    }

    public List<QueuePiece> getPath(){
        return visitedPieces;
    }

    public String getPathString(){
        StringBuilder builder = new StringBuilder();

        for (QueuePiece piece : visitedPieces){
            builder.append(" Piece ").append(piece.getX()).append(" ").append(piece.getY());
        }

        return builder.toString();
    }
}
