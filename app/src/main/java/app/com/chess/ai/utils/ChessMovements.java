package app.com.chess.ai.utils;

import android.content.Intent;

import java.util.ArrayList;

import app.com.chess.ai.enums.ChessPieceEnum;
import app.com.chess.ai.models.global.ChessPiece;

public class ChessMovements {
    public ChessMovements() {
    }

    public ArrayList<Integer> getPawnMovement(int position) {
        ArrayList<Integer> movementList = new ArrayList<>();
        position = position - 8;
        movementList.add(position);

        return movementList;
    }

    public ArrayList<Integer> getBlackPawnMovement(int position) {
        ArrayList<Integer> movementList = new ArrayList<>();
        position = position + 8;
        movementList.add(position);

        return movementList;
    }

    public ArrayList<Integer> getRookMovement(int position) {
        ArrayList<Integer> movementList = new ArrayList<>();
        int bottom = position + 8;
        movementList.add(bottom);
        int top = position - 8;
        movementList.add(top);
        int left = 0;
        int right = 0;
        if (position % 8 != 0) {
            left = position - 1;
            movementList.add(left);
        }
        if (((position + 1) % 8) != 0) {
            right = position + 1;
            movementList.add(right);
        }
        for (int i = 0; i < 10; i++) {
            if (bottom < 63) {
                bottom = bottom + 8;
                movementList.add(bottom);
            }
            if (top > 0) {
                top = top - 8;
                movementList.add(top);
            }
            if (left % 8 != 0) {
                left = left - 1;
                movementList.add(left);
            } else {
                if (movementList.contains(left % 8)) {
                    movementList.add(left % 8);
                }
            }
            int rightRemainder = ((right + 1) % 8);
            if (rightRemainder != 0 && right != 0) {
                right = right + 1;
                movementList.add(right);
            }
        }
        return movementList;
    }

    public ArrayList<Integer> getRook(int position, ArrayList<ChessPiece> chessPieceArrayList) {
        ArrayList<Integer> movementList = new ArrayList<>();
        boolean stopTop = false;
        boolean stopBottom = false;
        boolean stopRight = false;
        boolean stopLeft = false;
        int bottom = position + 8;
        if (bottom <= 63 && chessPieceArrayList.get(bottom).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopBottom) {
            movementList.add(bottom);
        } else {
            stopBottom = true;
        }
        int top = position - 8;
        if (top > 0 && chessPieceArrayList.get(top).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopTop) {
            movementList.add(top);
        } else {
            stopTop = true;
        }
        int left = 0;
        int right = 0;
        if (position % 8 != 0) {
            left = position - 1;
            if (chessPieceArrayList.get(left).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopLeft) {
                movementList.add(left);
            } else {
                stopLeft = true;
            }
        }
        if (((position + 1) % 8) != 0) {
            right = position + 1;
            if (chessPieceArrayList.get(right).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopRight) {
                movementList.add(right);
            } else {
                stopRight = true;
            }
        }
        for (int i = 0; i < 10; i++) {
            bottom = bottom + 8;
            if (bottom <= 63 && chessPieceArrayList.get(bottom).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopBottom) {
                movementList.add(bottom);
            } else {
                stopBottom = true;
            }
            if (top > 0) {
                top = top - 8;
                if (top > 0 && chessPieceArrayList.get(top).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopTop) {
                    movementList.add(top);
                } else {
                    stopTop = true;
                }
            }
            if (left % 8 != 0) {
                left = left - 1;
                if (chessPieceArrayList.get(left).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopLeft) {
                    movementList.add(left);
                } else {
                    stopLeft = true;
                }
            } else {
                if (movementList.contains(left % 8)) {
                    if (chessPieceArrayList.get(left).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopLeft) {
                        movementList.add(left);
                    } else {
                        stopLeft = true;
                    }
                }
            }
            int rightRemainder = ((right + 1) % 8);
            if (rightRemainder != 0 && right != 0) {
                right = right + 1;
                if (chessPieceArrayList.get(right).piece == ChessPieceEnum.EMPTY.getChessPiece() && !stopRight) {
                    movementList.add(right);
                } else {
                    stopRight = true;
                }
            }
        }
        return movementList;
    }

    public ArrayList<Integer> getBishopMovement(int position, ArrayList<ChessPiece> chessPieceArrayList) {
        boolean stopRT = false;
        boolean stopLT = false;
        boolean stopRB = false;
        boolean stopLB = false;

        ArrayList<Integer> movementList = new ArrayList<>();
        int lb = position + 7;
        int rb = position + 9;
        int lt = position - 9;
        int rt = position - 7;

        if (lb > 0 && lb < 64) {
            if ((position % 8 != 0) && !stopLB) {
                if (lb < chessPieceArrayList.size()) {
                    if (chessPieceArrayList.get(lb) != null && chessPieceArrayList.get(lb).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                        movementList.add(lb);
                    } else {
                        stopLB = true;
                    }
                } else {
                    stopLB = true;
                }
            }
        }
        if (lt > 0 && lt < 64) {
            if ((position % 8 != 0) && !stopLT) {
                if (lt > 0) {
                    if (chessPieceArrayList.get(lt) != null && chessPieceArrayList.get(lt).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                        movementList.add(lt);
                    } else {
                        stopLT = true;
                    }
                } else {
                    stopLT = true;
                }
            }
        }
        if (rt > 0 && rt < 64) {
            if ((((position + 1) % 8) != 0) && !stopRT) {
                if (rt > 0) {
                    if (chessPieceArrayList.get(rt) != null && chessPieceArrayList.get(rt).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                        movementList.add(rt);
                    } else {
                        stopRT = true;
                    }
                } else {
                    stopRT = true;
                }
            }
        }
        if (rb > 0 && rb < 64) {
            if ((((position + 1) % 8) != 0) && !stopRB) {
                if (rb < chessPieceArrayList.size()) {
                    if (chessPieceArrayList.get(rb) != null && chessPieceArrayList.get(rb).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                        movementList.add(rb);
                    } else {
                        stopRB = true;
                    }
                } else {
                    stopRB = true;
                }
            }
        }
        for (int i = 0; i < 10; i++) {

            if (lb > 0 && lb < 64) {
                if ((((lb + 1) % 8) != 0) && (lb % 8 != 0) && !stopLB) {
                    lb = lb + 7;
                    if (lb < chessPieceArrayList.size()) {
                        if (chessPieceArrayList.get(lb) != null && chessPieceArrayList.get(lb).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                            movementList.add(lb);
                        } else {
                            stopLB = true;
                        }
                    } else {
                        stopLB = true;
                    }
                }
            }
            if (lt > 0 && lt < 64) {
                if ((((lt + 1) % 8) != 0) && (lt % 8 != 0) && !stopLT) {
                    lt = lt - 9;
                    if (lt > 0) {
                        if (chessPieceArrayList.get(lt) != null && chessPieceArrayList.get(lt).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                            movementList.add(lt);
                        } else {
                            stopLT = true;
                        }
                    }
                } else {
                    stopLT = true;
                }
            }
            if (rt > 0 && rt < 64) {
                if ((((rt + 1) % 8) != 0) && !stopRT) {
                    rt = rt - 7;
                    if (rt > 0) {
                        if (chessPieceArrayList.get(rt) != null && chessPieceArrayList.get(rt).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                            movementList.add(rt);
                        } else {
                            stopRT = true;
                        }
                    }
                } else {
                    stopRT = true;
                }
            }
            if (rb > 0 && rb < 64) {
                if ((((rb + 1) % 8) != 0) && !stopRB) {
                    rb = rb + 9;
                    if (rb < chessPieceArrayList.size()) {
                        if (chessPieceArrayList.get(rb) != null && chessPieceArrayList.get(rb).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                            movementList.add(rb);
                        } else {
                            stopRB = true;
                        }
                    }
                } else {
                    stopRB = true;
                }
            }
        }
        return movementList;
    }

    public ArrayList<Integer> getQueenMovement(int position, ArrayList<ChessPiece> chessPieceArrayList) {
        ArrayList<Integer> rookMovements = getRook(position, chessPieceArrayList);
        ArrayList<Integer> bishopMovements = getBishopMovement(position, chessPieceArrayList);
        bishopMovements.addAll(rookMovements);

        return bishopMovements;
    }

    public ArrayList<Integer> getKingMovement(int position, ArrayList<ChessPiece> chessPieceArrayList) {
        ArrayList<Integer> movementList = new ArrayList<>();
        int lb = position + 7;
        int rb = position + 9;
        int lt = position - 9;
        int rt = position - 7;
        int bottom = position + 8;
        int top = position - 8;
        int left = position - 1;
        int right = position + 1;

        if (lb > 0 && lb < 63) {
            if (chessPieceArrayList.get(lb).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(lb);
            }
        }
        if (rb > 0 && rb < 63) {
            if (chessPieceArrayList.get(rb).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(rb);
            }
        }
        if (lt > 0 && lt < 63) {
            if (chessPieceArrayList.get(lt).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(lt);
            }
        }
        if (rt > 0 && rt < 63) {
            if (chessPieceArrayList.get(rt).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(rt);
            }
        }
        if (bottom > 0 && bottom < 63) {
            if (chessPieceArrayList.get(bottom).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(bottom);
            }
        }
        if (left > 0 && left < 63) {
            if (chessPieceArrayList.get(left).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(left);
            }
        }
        if (right > 0 && right < 63) {
            if (chessPieceArrayList.get(right).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(right);
            }
        }
        if (top > 0 && top < 63) {
            if (chessPieceArrayList.get(top).piece == ChessPieceEnum.EMPTY.getChessPiece()) {
                movementList.add(top);
            }
        }
        return movementList;
    }

    public ArrayList<Integer> getKnightMovement(int position, ArrayList<ChessPiece> chessPieceArrayList) {
        ArrayList<Integer> movementList = new ArrayList<>();
        int p1 = position - 17;
        int p2 = position - 15;
        int p3 = position + 17;
        int p4 = position + 15;
        int p5 = position - 6;
        int p6 = position - 10;
        int p7 = position + 6;
        int p8 = position + 10;

        if ((position % 8 != 0) && ((position - 1) % 8 != 0)) {
            movementList.add(p6);
            movementList.add(p7);
        }
        if (((position + 1) % 8 != 0) && ((position + 2) % 8 != 0)) {
            movementList.add(p5);
            movementList.add(p8);
        }
        if ((position + 1) % 8 != 0) {
            movementList.add(p2);
            movementList.add(p3);
        }
        if (position % 8 != 0) {
            movementList.add(p1);
            movementList.add(p4);
        }
        return movementList;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

}
