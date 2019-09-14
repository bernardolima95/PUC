// LPA_3.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include<vector>
using namespace std;

int main(){
	int nTestes = 0, tamanhoRede = 0, nPares = 0;
	int matriz[10][10] = {};
	int par1, par2;
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
