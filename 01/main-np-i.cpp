#include <iostream>
#include <vector>

using namespace std;

int main(int argc, char *argv[]) {
  if (argc != 2) {
    exit(EXIT_FAILURE);
  }
  size_t n = stoi(argv[1]);

  vector<vector<int>> a(n, vector<int>(n));
  vector<vector<int>> b(n, vector<int>(n));
  vector<vector<int>> c(n, vector<int>(n));

  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < n; ++j) {
      a[i][j] = b[i][j] = i + j;
    }
  }

  #pragma omp parallel for
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < n; ++j) {
      for (size_t k = 0; k < n; ++k) {
        c[i][j] += a[i][k] * b[k][j];
      }
    }
  }

  int trash = 0;
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < n; ++j) {
      trash += c[i][j];
    }
  }

  cout << trash << "\n";
}
