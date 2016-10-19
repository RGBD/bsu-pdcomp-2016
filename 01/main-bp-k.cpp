#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main(int argc, char *argv[]) {
  if (argc != 3) {
    exit(EXIT_FAILURE);
  }
  size_t n = stoi(argv[1]);
  size_t block_size = stoi(argv[2]);

  vector<vector<int>> a(n, vector<int>(n));
  vector<vector<int>> b(n, vector<int>(n));
  vector<vector<int>> c(n, vector<int>(n));

  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < n; ++j) {
      a[i][j] = b[i][j] = i + j;
    }
  }

  for (size_t ii = 0; ii < n; ii += block_size) {
    size_t i_stop = min(ii + block_size, n);
    for (size_t jj = 0; jj < n; jj += block_size) {
      size_t j_stop = min(jj + block_size, n);
      for (size_t kk = 0; kk < n; kk += block_size) {
        size_t k_stop = min(kk + block_size, n);

        for (size_t i = ii; i < i_stop; ++i) {
          for (size_t j = jj; j < j_stop; ++j) {
            #pragma omp parallel for
            for (size_t k = kk; k < k_stop; ++k) {
              #pragma omp atomic
              c[i][j] += a[i][k] * b[k][j];
            }
          }
        }
        
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
