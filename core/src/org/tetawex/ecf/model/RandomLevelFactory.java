package org.tetawex.ecf.model;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.OrderedSet;
import org.tetawex.ecf.util.IntVector2;
import org.tetawex.ecf.util.RandomProvider;

import java.util.function.*;

import java.util.*;

/**
 * Created by 1 on 06.06.2017.
 */
public class RandomLevelFactory
{
    private Cell[][] cellArray;
    private Set<Cell> cellSetAll;
    private Set<Cell> cellSetNoElements;
    private Set<Cell> cellSetWithElements;
    private RandomXS128 random;
    private int width,
                height;
    private int mana = 2;

    public RandomLevelFactory(int seed, int width, int height){
        RandomProvider.setSeed(seed);
        random = RandomProvider.getRandom();
        this.width = width;
        this.height = height;
        cellArray = CellArrayFactory.generateEmptyCellArray(width, height);
        createNullCells();
        createCellSets();
        fillTheBoard(width+height);
    }

    public int getMana(){
        return mana;
    }

    public Cell[][] getTheBoard(){
        return cellArray;
    }

    private void createNullCells(){

        int iterations = Math.min(width, height)+1;
        int x,y;
        for(int i =0; i<iterations;++i){
            if (random.nextBoolean()) {
                x = random.nextInt(width);
                y = random.nextInt(height);
                cellArray[x][y] = null;
            }
        }
    }

    private void createCellSets(){
        cellSetAll = new HashSet<Cell>();
        cellSetWithElements = new HashSet<Cell>();
        cellSetNoElements = new HashSet<Cell>();
        for (int x = 0; x < width; ++x){
            for (int y = 0; y < height; ++y){
                Cell cell = getCell(new IntVector2(x, y));
                if (cell != null) {
                    cellSetAll.add(cell);
                    cellSetNoElements.add(cell);
                }
            }
        }
    }

    private void fillTheBoard(int iterations){
        preFill();
        for(int i = 0; i < iterations; ++i){
            int rnd = random.nextInt(100);
            if (rnd < 20) {
                moveCell();
            }else if(rnd < 30){
                splitCell();
            }else if(rnd < 50){
                createOppositeElements();
            }else if(rnd < 100){
                createIdenticalElements();
            }
        }
    }

    private void preFill(){
        Cell cell1, cell2;
        cell1 = getRandomCellFromSet(cellSetAll);
        cell2 = getRandomNeighbour(cell1);
        cellSetNoElements.remove(cell1);
        cellSetWithElements.add(cell1);
        cellSetNoElements.remove(cell2);
        cellSetWithElements.add(cell2);
        cell1.getElements().add(Element.FIRE);
        cell2.getElements().add(Element.WATER);

        cell1 = getRandomCellFromSet(cellSetAll);
        cell2 = getRandomNeighbour(cell1);
        cellSetNoElements.remove(cell1);
        cellSetWithElements.add(cell1);
        cellSetNoElements.remove(cell2);
        cellSetWithElements.add(cell2);
        cell1.getElements().add(Element.LIGHT);
        cell2.getElements().add(Element.SHADOW);

        cell1 = getRandomCellFromSet(cellSetAll);
        cell2 = getRandomNeighbour(cell1);
        cellSetNoElements.remove(cell1);
        cellSetWithElements.add(cell1);
        cellSetNoElements.remove(cell2);
        cellSetWithElements.add(cell2);
        cell1.getElements().add(Element.EARTH);
        cell2.getElements().add(Element.AIR);
    }

    private void moveCell(){
        Cell from = getRandomCellFromSet(cellSetWithElements);
        if (from == null) return;
        List<Cell> neighbours = getNeighoubrsEmpty(from);
        if (neighbours.size() != 0){
            ++mana;
            Cell to = getRandom(neighbours);
            cellSetNoElements.remove(to);
            cellSetWithElements.add(to);
            cellSetWithElements.remove(from);
            cellSetNoElements.add(from);

            for(int i = 0; i < from.getElements().size; ++i){
                Element element = from.getElements().first();
                to.getElements().add(element);
                from.getElements().remove(element);
            }
        }
    }

    private void splitCell(){
        Cell from = null;
        for(int i = 0; i<5; ++i){
            from = getRandomCellFromSet(cellSetWithElements);
            if(from != null && from.getElements().size > 1) break;
        }
        if(from == null || from.getElements().size == 1) return;

        List<Cell> neighbours = getNeighoubrsEmpty(from);
        if (neighbours.size() != 0){
            ++mana;
            Cell to = getRandom(neighbours);
            cellSetNoElements.remove(to);
            cellSetWithElements.add(to);
            int amount = random.nextInt(from.getElements().size-1)+1;
            if (amount == from.getElements().size){
                cellSetWithElements.remove(from);
                cellSetNoElements.add(from);
            }
            for(int i = 0; i < amount; ++i){
                Element element = from.getElements().first();
                to.getElements().add(element);
                from.getElements().remove(element);
            }
        }
    }

    private void createOppositeElements(){
        Cell from = getRandomCellFromSet(cellSetNoElements);
        if (from == null) return;
        List<Cell> neighbours = getNeighoubrsEmpty(from);
        if (neighbours.size() != 0){

            Cell to = getRandom(neighbours);
            cellSetNoElements.remove(from);
            cellSetWithElements.add(from);
            cellSetNoElements.remove(to);
            cellSetWithElements.add(to);

            from.setElements(ElementFunctions.generateRandomElementSet());
            int size = from.getElements().size;
            if(mana > 1+size){
                mana-=size;
            }else{
                mana = 1;
            }
            for (Element element:
                 from.getElements()) {
                to.getElements().add(Element.getOpposite(element));
            }
        }
    }

    private void createIdenticalElements(){
        Cell from = getRandomCellFromSet(cellSetWithElements);
        if (from == null) return;
        List<Cell> neighbours = getNeighoubrsEmpty(from);
        if (neighbours.size() != 0){
            ++mana;
            Cell to = getRandom(neighbours);
            cellSetNoElements.remove(to);
            cellSetWithElements.add(to);
            for (Element element:
                    from.getElements()) {
                to.getElements().add(element);
            }
        }
    }

    private Cell getCell(IntVector2 pos){
        if (!(0 <= pos.x && pos.x < width)){
            return null;
        }
        if (!(0 <= pos.y && pos.y < height)){
            return null;
        }
        return cellArray[pos.x][pos.y];
    }

    public List<Cell> getNeighbours(Cell cell){
        List<Cell> res = new ArrayList<Cell>();
        IntVector2 pos = cell.getPosition();
        Cell neighbour;
        int x = pos.x;
        int y = pos.y;
        if(x%2 != 0){
            neighbour = getCell(new IntVector2(x-1, y+1));
            if (neighbour != null) res.add(neighbour);

            neighbour = getCell(new IntVector2(x+1, y+1));
            if (neighbour != null) res.add(neighbour);
        }else{
            neighbour = getCell(new IntVector2(x-1, y-1));
            if (neighbour != null) res.add(neighbour);

            neighbour = getCell(new IntVector2(x+1, y-1));
            if (neighbour != null) res.add(neighbour);
        }
        neighbour = getCell(new IntVector2(x-1, y));
        if (neighbour != null) res.add(neighbour);

        neighbour = getCell(new IntVector2(x+1, y));
        if (neighbour != null) res.add(neighbour);

        neighbour = getCell(new IntVector2(x, y+1));
        if (neighbour != null) res.add(neighbour);

        neighbour = getCell(new IntVector2(x, y-1));
        if (neighbour != null) res.add(neighbour);

        return res;
    }

    private List<Cell> getNeighoubrsEmpty(Cell cell){
        List<Cell> neighbours = getNeighbours(cell);
        List<Cell> res = new ArrayList<Cell>();
        for (Cell neighbour:
             neighbours) {
            if(neighbour.getElements().size == 0){
                res.add(neighbour);
            }
        }

        return res;
    }

    private List<Cell> getNeighboursNotEmpty(Cell cell){
        List<Cell> neighbours = getNeighbours(cell);
        List<Cell> res = new ArrayList<Cell>();
        for (Cell neighbour:
                neighbours) {
            if(neighbour.getElements().size != 0){
                res.add(neighbour);
            }
        }

        return res;
    }

    private Cell getRandom(List<Cell> cells){
        return cells.get(random.nextInt(cells.size()));
    }

    private Cell getRandomNeighbour(Cell cell){
        List<Cell> neighbours = getNeighbours(cell);
        return neighbours.get(random.nextInt(neighbours.size()));
    }

    private Cell getRandomCellFromSet(Set<Cell> set){
        if (set.size() == 0) return null;
        int index = random.nextInt(set.size());
        Iterator<Cell> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }
}
