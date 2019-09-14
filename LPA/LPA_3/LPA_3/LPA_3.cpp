// LPA_3.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include<vector>
#define n 300001
using namespace std;
int matriz[15000][15000], visitado[15000], visitar[15000], pilha[15000], topo;

void zeraMatriz(int tamanhoRede) {
	for (int i = 1; i < tamanhoRede + 1; ++i) {
		for (int j = 1; j < tamanhoRede + 1; ++j) {
			matriz[i][j] = 0;
		}
		std::cout << std::endl;
	}
}

void imprimeMatriz(int tamanhoRede) {
	for (int i = 1; i < tamanhoRede + 1; ++i){
		for (int j = 1; j < tamanhoRede + 1; ++j){
			std::cout << matriz[i][j] << ' ';
		}
		std::cout << std::endl;
    }
}

void contaMatriz(int tamanhoRede) {
	for (int i = 1; i < tamanhoRede + 1; ++i) {
		for (int j = 1; j < tamanhoRede + 1; ++j) {
		}
	}
}

int contaVertices(int tamanhoRede, int vertice) {
	visitado[vertice] = 1;
	int k = 1, nVertices = 0;
	while (k < tamanhoRede)
	{
		for (int j = tamanhoRede; j >= 1; j--)
			if (matriz[vertice][j] != 0 && visitado[j] != 1 && visitar[j] != 1)
			{
				visitar[j] = 1;
				pilha[topo] = j;
				nVertices++;
				topo++;
			}
		vertice = pilha[--topo];
		cout << vertice << " ";
		k++;
		visitar[vertice] = 0; visitado[vertice] = 1;
	}

	std::fill_n(visitar, tamanhoRede, 0);
	std::fill_n(visitado, tamanhoRede, 0);
	std::fill_n(pilha, tamanhoRede, 0);

	return nVertices + 1;
}

int main(){
	int nTestes = 0, tamanhoRede = 0, nPares = 0;
	int par1, par2;
	int vertice;
	int nVertices;
	std::cin >> nTestes;
	for (int i = 0; i < nTestes; i++) {
		std::cin >> tamanhoRede;
		std::cin >> nPares;
		for (int j = 0; j < nPares; j++) {
			std::cin >> par1;
			std::cin >> par2;
			matriz[par1][par2] = 1;
			matriz[par2][par1] = 1;
		}
		std::cin >> vertice;
		imprimeMatriz(tamanhoRede);
		std::cout << std::endl;
		cout << vertice << " ";
		nVertices = contaVertices(tamanhoRede, vertice);
		std::cout << nVertices;
		zeraMatriz(tamanhoRede);
		//imprimeMatriz(tamanhoRede);
	}

}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
