import java.util.LinkedList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;


class PrimsAlgorithm 
{

    //static File log = new File("Prim_Trace1.txt");
    //graph edge,source,target and weight
     static class Edge 
     {
        int source;
        int target;
        int weight;
        
        public Edge(int source, int target, int weight) 
        {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }
    }

    static class HeapNode
    {
        int vertex;
        int key;
    }

    static class total
    {
        static int vertices = 3892;
    }

    static class MSTSET
    {
        int parent;
        int weight;
    }

    //-------------- Graph Class --------------------

    static class Graph 
    {
        int vertices;
        LinkedList<Edge>[] adjlist;

        Graph(int vertices) 
        {
            this.vertices = vertices;

            // creating the adjency list
            adjlist = new LinkedList[vertices];
            
            //initializing the adj list with the size equal to the number of vertices 
            for (int i = 0; i <vertices ; i++) 
            {
                adjlist[i] = new LinkedList<>();
            }
        }

        // adding the edges to the graph
        public void addEdge(int source, int target, int weight) 
        {
            Edge edge = new Edge(source, target, weight);
            adjlist[source].addFirst(edge);

            edge = new Edge(target, source, weight);
            adjlist[target].addFirst(edge); 
        }

        public void primAlgo() throws IOException
        {

            //keeping track of the vertices currently in min heap
            boolean[] currentHeap = new boolean[vertices];

            MSTSET[] mstSet = new MSTSET[vertices];

            // used to store the value of each vertex in order to determine if update 
            // in min heap is required

            int [] key = new int[vertices];

            //create heapNode for all the vertices
            HeapNode [] heapNodes = new HeapNode[vertices];

            for (int i = 0; i <vertices ; i++) 
            {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;

                //intialzing all keys to the maximum (infinity value)

                heapNodes[i].key = Integer.MAX_VALUE;
                mstSet[i] = new MSTSET();
                mstSet[i].parent = -1;
                currentHeap[i] = true;
                key[i] = Integer.MAX_VALUE;
            }

            //decreasing the key for the first index, setting it to zero
            heapNodes[0].key = 0;

            //inserting all the vertices to the MinHeap
            BinaryMinHeap minHeap = new BinaryMinHeap(vertices);

            //inserting all the vertices to priority queue
           
            for (int i = 0; i <vertices ; i++) 
            {
                minHeap.insert(heapNodes[i]);
                
              
            }

            System.out.println();
            // PrintWriter out = new PrintWriter(new FileWriter(log, true));
            // out.append("==========Extracting min element========== ");
            // out.append("\n");
            // out.close();

            //System.out.println("Extracting min element: ");
            //System.out.println();


            //while minHeap is not empty
            while(!minHeap.isEmpty())
            {
                
                //extract the minumum node 
                HeapNode extractedNode = minHeap.extractMin();
                // set extracted vertex to false value
                int extractedVertex = extractedNode.vertex;
                currentHeap[extractedVertex] = false;
               

                //iterate through all the adjacent vertices
                LinkedList<Edge> list = adjlist[extractedVertex];
                for (int i = 0; i <list.size() ; i++) 
                {
                    Edge edge = list.get(i);

                    //only if edge target is present in heap
                    if(currentHeap[edge.target]) 
                    {
                        int target = edge.target;
                        int newKey = edge.weight;
                       

                        //check if updated key < existing key, if yes, update 
                        if(key[target]>newKey) 
                        {
                            DecreaseKey(minHeap, newKey, target);

                            //update the parent node for target
                            mstSet[target].parent = extractedVertex;
                            mstSet[target].weight = newKey;
                            key[target] = newKey;
                            
                           
                        }

                      
                    }
                 
                }    
                
            }
            
             printPRIMMST(mstSet);
        }

        public void DecreaseKey(BinaryMinHeap minHeap, int newKey, int vertex) throws IOException
        {

            //get the index of the key needed to be decreased;
            int index = minHeap.positions[vertex];

            //getting the vertex and updating its value
            HeapNode node = minHeap.arr[index];
            node.key= newKey;
         
            minHeap.minHeapify(index);
        }

        // print the MST
        public void printPRIMMST(MSTSET[] finalResult) throws IOException
        {
            int total_weight = 0;
            int edges = 0;
         
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("prim_output.csv"))) 
            {
                System.out.println("PRIM MST: ");
                for (int i = 1; i <vertices ; i++)
                {
                    bw.write(i+","+finalResult[i].parent+","+finalResult[i].weight+"\n");
                    System.out.println("Edge: " + i + " ---- " + finalResult[i].parent + " Weight: " + finalResult[i].weight );
                    total_weight += finalResult[i].weight;
                    edges= i;     
                }
                
           }
             
        
            System.out.println("Total minimum key: " +  total_weight);
            System.out.println("Total edges in MST: " + edges);
            double v1 = total.vertices;
            double v2 = edges;
            double degree = (v2/v1)*2;
            System.out.println("The Average Degree: " + degree);
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("total_cost_prim.txt"));
             bw1.write("Total Weight: "  + total_weight +"\n");
             bw1.write("The Average Degree: " + degree);
             bw1.close();
        }
    }

  
  
  
    static class BinaryMinHeap 
    {
        int capacity; // the capacity of min heap
        int size;
        HeapNode[] arr;
        int [] positions; //this is needed for the function decrease key
        

        

        
        public BinaryMinHeap(int capacity) 
        {
            
            this.capacity = capacity;
            arr = new HeapNode[capacity + 1];
            positions = new int[capacity];
            arr[0] = new HeapNode();
            arr[0].key = Integer.MIN_VALUE;
            arr[0].vertex=-1;
            size = 0;
        }
       
       
       
        public void display() throws IOException 
        {
           
            //PrintWriter out = new PrintWriter(new FileWriter(log, true));
            for (int i = 1; i <=size; i++) 
            {
                
               
                //System.out.println("vertex: " + arr[i].vertex + " key: " + arr[i].key + "\n");
               // out.append("vertex: " + arr[i].vertex + " key: " + arr[i].key + "\n");
               
              
 
               
            }
            
        //   out.append("----------------------------");
        //   out.append("\n");
        //   out.close();
       
        //System.out.println();
        //System.out.println("-------------------------");
        //System.out.println();
      
       
        }
        

        public void insert(HeapNode x) throws IOException 
        {
            size++;
            int i = size;
            arr[i] = x;
            // PrintWriter out = new PrintWriter(new FileWriter(log, true));
            // out.append("\n");
            // out.append("Insertion at iteration:" + i + " vertex: "+arr[i].vertex + " key: " + arr[i].key);
            // out.append("\n");
            //out.close();
           // System.out.println("Insertion at iteration:" + i + " vertex: "+arr[i].vertex + " key: " + arr[i].key);
            //System.out.println();
            positions[x.vertex] = i;
            minHeapify(i);
           
           
        }

        

        public void minHeapify(int pos) throws IOException 
        {
            int parentIndex = pos/2;
            int currentIndex  = pos;
            while (currentIndex  > 0 && arr[parentIndex ].key > arr[currentIndex ].key) 
            {
                HeapNode currentNode = arr[currentIndex ];
                HeapNode parentNode = arr[parentIndex ];

                //swap the positions
                positions[currentNode.vertex] = parentIndex ;
                positions[parentNode.vertex] = currentIndex ;
                // swap the nodes
                swap(currentIndex ,parentIndex );
                currentIndex = parentIndex ;
                parentIndex  = parentIndex /2;
            }

             display();
           
           
        }

        public HeapNode extractMin() 
        {
            // taking out element from the root
            HeapNode minimuNode = arr[1];

            HeapNode lastNode = arr[size];
            // update the indexes and move the last node to the top
            positions[lastNode.vertex] = 1;
            arr[1] = lastNode;
            arr[size] = null;
            HeapifyDown(1);
            size--;
            return minimuNode;
        }

        public void HeapifyDown(int k)
         {
            int smallest = k;
            int leftChild = 2 * k;
            int rightChild = 2 * k+1;
            if (leftChild < heapsize() && arr[smallest].key > arr[leftChild].key) 
            {
                smallest = leftChild;
            }
            if (rightChild < heapsize() && arr[smallest].key > arr[rightChild].key) 
            {
                smallest = rightChild;
            }
            if (smallest != k)
             {

                HeapNode smallestNode = arr[smallest];
                HeapNode kNode = arr[k];

                //swap the positions
                positions[smallestNode.vertex] = k;
                positions[kNode.vertex] = smallest;
                swap(k, smallest);
                // repeating until all the nodes are at their correct position
                HeapifyDown(smallest);
            }
        }

        public void swap(int x, int y) 
        {
            HeapNode temp = arr[x];
            arr[x] = arr[y];
            arr[y] = temp;
        }

        public boolean isEmpty() 
        {
            return size == 0;
        }

        public int heapsize()
        {
            return size;
        }
    }

    // ----Main----
    public static void main(String[] args) throws IOException 
    {
        int vertices = total.vertices;
        String sample = ",";
        String mystring;
        Graph graph = new Graph(vertices);

        try
        {
            BufferedReader brdrd = new BufferedReader(new FileReader("tvshow_edges.csv"));
            while ((mystring = brdrd.readLine()) != null)  //Reads a line of text
            {
                String[] s1 = mystring.split(sample);  //utilized to split the string
                graph.addEdge(Integer.parseInt(s1[0]),Integer.parseInt(s1[1]),Integer.parseInt(s1[2]));
            }
        }
        catch (IOException e) //catches exception in the try block
        {

            e.printStackTrace(); //Prints this throwable and its backtrace
        }


    
        final long startTime = System.nanoTime();
        graph.primAlgo();
        final long endTime = System.nanoTime();

        System.out.println("Total execution time in milliseconds: " + ((endTime - startTime)/1000000));
      
      
        
        try 
        {
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("D:\\PRIM_TotalExceutionTime1.txt"));
            
            bw1.write("Total execution time in milliseconds: " + ((endTime - startTime)/1000000));
            bw1.close();
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
        

    


      
       
        
    }
}