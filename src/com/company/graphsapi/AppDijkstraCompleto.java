package com.company.graphsapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import edu.princeton.cs.algs4.*;
import sun.text.resources.cldr.ig.FormatData_ig;

public class AppDijkstraCompleto {
		
	public static void main(String[] args) {

		// Exemplo: lendo os dados do arquivo com as classes do Sedgewick
		EdgeWeightedDigraph dig = new EdgeWeightedDigraph(new In("1000EWD.txt"));
		System.out.println("Total vértices: "+dig.V());
		System.out.println("Total arestas: "+dig.E());	
		
		int s = 0; // Vértice de origem
		
		double[] distTo = new double[dig.V()];
		DirectedEdge[] edgeTo = new DirectedEdge[dig.V()];
		IndexMinPQ<Double> minheap = new IndexMinPQ<>(dig.V());

		// Inicializa o array distTo para o maior valor possível
		// armazenável em um double (POSITIVE_INFINITY)
		for(int v=0; v<dig.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
		}
		distTo[s] = 0; // distância dele até ele mesmo => 0
		
		minheap.insert(s, 0.0); // insere primeiro elemento da árvore (o vértice inicial)
		
		// Enquanto houver algum vértice na fila de prioridade...
		while(!minheap.isEmpty())
		{
			// Retira o primeiro, isto é, o vértice com menor distTo
			int v = minheap.delMin();
			// Para cada aresta a partir dele...
			for(DirectedEdge e: dig.adj(v)) 
			{
				int ini = e.from();
				int fim = e.to();
				// Novo caminho: soma do acumulado até este vértice + o peso da aresta
				double novaDist = distTo[ini] + e.weight();
				// Se o novo caminho for melhor que o que estiver armazenado lá...
				if(distTo[fim] > novaDist)
				{
					// Este passa a ser o melhor (mais curto)
					distTo[fim] = novaDist;
					edgeTo[fim] = e;
					// Se o vértice já estiver na fila de prioridade...
					if(minheap.contains(fim))
						// Diminui o valor associado
						minheap.decreaseKey(fim, distTo[fim]);
					else
						// Caso contrário, insere o novo vértice
						minheap.insert(fim, distTo[fim]);
				}
			}
		}
		
		for(int v=0; v<dig.V(); v++) {
			System.out.println(v+": "+edgeTo[v]);
		}

		// Descomente as linhas abaixo para visualizar os números dos
		// vértices e o vértice inicial
//		String styleSheet =
//	            "node {" +
//	            "	fill-color: black;" +
//	            "   text-size: 20;" +
//	            "}" +
//	            "node.marked {" +
//	            "	fill-color: red;" +
//	            "}" +
//	            "node.start {" +
//	            "   fill-color: green;" +
//	            "   text-size: 30;"+
//	            "}";

		// Descomente as linhas abaixo para ocultar os números dos vértices
		// (útil para grafos GRANDES)
		String styleSheet =
				"node { "+
				"	size: 8px; "+
				"	fill-color: #777; "+
				"	text-mode: hidden; "+
				"	z-index: 0; "+
				"}" +
				"node.marked {" +
				"   fill-color: red;" +
				"}" +
	            "node.start {" +
	            "   fill-color: green;" +
	            "}" +
				"edge { "+
				"	shape: line;"+
				"	fill-color: #222;"+
				"	arrow-size: 3px, 2px;"+
				"}";
		
		// API do Graphstream:
	
		// Cria um grafo
		Graph g = new SingleGraph("Dijkstra");
		
		for(int v=0; v<dig.V(); v++) {
			Node n = g.addNode(v+"");
			n.addAttribute("ui.label", v);
			if(edgeTo[v] != null)
				n.addAttribute("ui.class", "marked");			
		}
		Node n = g.getNode(s);
		n.setAttribute("ui.class", "start");
		for(DirectedEdge e: dig.edges()) {
			int v1 = e.from();
			int v2 = e.to();
			double w = e.weight();
			Edge ne = g.addEdge(v1+"->"+v2, v1, v2, true);			
			//ne.addAttribute("ui.label", w);
		}
		
		for(int v=0; v<dig.V(); v++)
		{
			if(edgeTo[v] != null) {
				Edge e = g.getEdge(edgeTo[v].from()+"->"+edgeTo[v].to());
				e.addAttribute("ui.style", "size: 2px; fill-color: red;");
			}				
		}
		 				
		/*
		// Exemplo: criando vértices (nodos), adicionando atributos (opcionalmente)
		Node a = g.addNode("A" );		
		// ui.label = rótulo do vértice
		a.addAttribute("ui.label", "A");
		// ui.classe = classe declarada na style sheet acima (pinta de vermelho)
		a.setAttribute("ui.class", "marked");
		// ui.style = outra forma de especificar o estilo do vértice. Neste caso, o tamanho é 30
		a.setAttribute("ui.style", "size: 30;");
		g.addNode("B" );
		g.addNode("C" );			

		// Exemplo: criando arestas
		// 4 parâmetros: nome da aresta, vértice inicial, vértice final, true se for dirigida
		Edge ab = g.addEdge("AB", "A", "B", true);
		// O rótulo da aresta equivale ao peso num grafo valorado
		ab.addAttribute("ui.label", "12.5");
		// Acrescenta um atributo weight para armazenar o peso numérico
		ab.addAttribute("weight", 12.5);
		// Usando ui.style para especificar o estilo: largura (size) e cor (fill-color)
		ab.addAttribute("ui.style", "size: 5px; fill-color: red;");	
		g.addEdge("BC", "B", "C", true);
		g.addEdge("CA", "C", "A", true);
		*/			
		
		/*
		// Exemplo: como acessar todos os vértices do grafo
		for(Node n: g) {
			System.out.print(n.getId()+": ");
			// Acessando cada aresta do vértice
			for(Edge e: n.getEachEdge())
				System.out.print(e.getNode0().getId()+"->"+e.getNode1().getId()+"("+e.getAttribute("weight")+") ");			
			System.out.println();
		}
		
		// Exemplo: como acessar todas as arestas do grafo
		for(Edge e:g.getEachEdge()) {
			System.out.println(e.getId()); // etc.		
		}
		*/
				
		// Solicita alta qualidade no desenho
//		g.addAttribute("ui.quality");
//		g.addAttribute("ui.antialias");
		
		// Caso se queira utilizar todos os recursos de CSS do visualizador avançado:
//		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		// Informa a style sheet a ser utilizada (se for o caso)
	    g.addAttribute("ui.stylesheet", styleSheet);
	    
	    // Exibe o grafo
		g.display();
				
	}
}
