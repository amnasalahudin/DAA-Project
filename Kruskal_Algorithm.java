import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

import java.util.Comparator;

public class Kruskal_Algorithm 
{
    //static File log = new File("Kruskal_Trace1.txt");
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

    static class total
    {
            static int vertices = 3892;
    }

    static class Graph 
    {
        int vertices;
        ArrayList<Edge> edges1 = new ArrayList<>();

        Graph(int vertices) 
        {
            this.vertices = vertices;
        }

        public void addEdge(int source, int target, int weight) throws IOException 
        {
            Edge edge = new Edge(source, target, weight);
            edges1.add(edge); 
            // PrintWriter out = new PrintWriter(new FileWriter(log, true));
            // out.append("Adding edge: --- Source: " + edge.source + " Target: " + edge.target + "\n");
            // out.close();
        }
    
        public void kruskalMST() throws IOException
        {
                PriorityQueue<Edge> p1 = new PriorityQueue<>(edges1.size(), Comparator.comparingInt(w -> w.weight));

                //adding all the edges to priority queue and sorting them based on weight
                for (int i = 0; i <edges1.size() ; i++) 
                {
                   
                        p1.add(edges1.get(i));
                }
                   

                //create a parent 
                int [] parent = new int[vertices];

                //make MST set
                makeMSTSet(parent);

                ArrayList<Edge> mst = new ArrayList<>();

                //number of edges to be taken equal to vertices - 1
                int index = 0;

             
            // PrintWriter out = new PrintWriter(new FileWriter(log, true));
            // out.append("----------Sorted edges:-------------- " + "\n");
                while(index<vertices-1)
                {
                        Edge edge = p1.remove();
                        

                        //out.append("Source: "+ edge.source + " " + "Target: " +edge.target+ " " + "Weight: " + edge.weight + "\n");
                       

                        //check if adding this edge creates a cycle
                        int sourceset = findcycle(parent, edge.source);
                        int targetset = findcycle(parent, edge.target);
                       

                        if(sourceset==targetset)
                        {
                           
                            // out.append("\n" + "Cycle created... Skipping the edge: " + edge.source + " ----- "  + edge.target + "\n");
                            //do not add it to mst as it will create a cycle
                        }
                        else 
                        {
                            //add it to the mst
                            mst.add(edge);

                            //out.append(  "\n" + "Adding edge to mst: " + edge.source + " ------ " + edge.target + "\n");
                            
                            
                            index++;
                            union(parent,sourceset,targetset);
                        }
                }
               // out.close();
                        
                        printMST(mst);
                }

            public void makeMSTSet(int [] parent)
            {
                
                
                //Make set- creating a new element with a parent pointer to itself.
                for (int i = 0; i <vertices ; i++) 
                {
                    parent[i] = i;
                   
                }
            }

                public int findcycle(int [] parent, int vertex)
                {
                    // if the find cycle function returns the same parent for both source
                    // and target then a cycle will form
                    
                    if(parent[vertex]!=vertex)
                    return findcycle(parent, parent[vertex]);
                    return vertex;
                    
                }

                // finds union of sets x and y
                public void union(int [] parent, int x, int y)
                {
                    int xroot = findcycle(parent, x);
                    int yroot= findcycle(parent, y);

                    //make x as parent of y
                    parent[yroot] = xroot;
                }

            public void printMST(ArrayList<Edge> edgeList) throws IOException
            {
                        int total_weight =0;
                        int edges1 = 0;

                              System.out.println("MST:");
                      
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter("kruskal_output.csv"))) {
                            for (int i = 0; i <edgeList.size() ; i++) 
                            {
                                Edge edge = edgeList.get(i);
                                bw.write(edge.source + ","+ edge.target + ","+ edge.weight+ "\n") ;
                                System.out.println("Edge: " + i + " source: " + edge.source + " target: " + edge.target + " weight: " + edge.weight);
                                
                                total_weight += edge.weight;
                                edges1 = i;
                            }
                        }
                    
                        System.out.println();
                        System.out.println("Total minimum key: " +  total_weight);
                        int e1 = edges1 + 1 ;
                        System.out.println("Total edges in MST: " + e1);
                        double v1 = total.vertices;
                        double v2 = (e1);
                        double degree = (v2/v1)*2;
                        System.out.println("The Average Degree: " + degree);
                        try (BufferedWriter bw1 = new BufferedWriter(new FileWriter("total_cost_kruskal.txt"))) 
                        {
                            bw1.write("Total Weight: "  + total_weight + "\n");
                            bw1.write("Total Average Degree: "  + degree);
                            bw1.close();
                        } catch (IOException e) 
                        {
                            
                            e.printStackTrace();
                        }
            
            }
            }


        public static void main(String[] args) throws IOException 
        {
            int vertices = total.vertices;
            Graph graph = new Graph(vertices);
            String sample = ",";
            String mystring;

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
        graph.kruskalMST();
        final long endTime = System.nanoTime();
        System.out.println("Total execution time in milliseconds: " + ((endTime - startTime)/1000000));

        try 
        {
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("D:\\KRUSKAL_TotalExceutionTime.txt"));
            
            bw1.write("Total execution time in milliseconds: " + ((endTime - startTime)/1000000));
            bw1.close();
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
    }
        }