# MatrixMultiplication

## Usage

Matrix can be created from `Array<Array<Double>>` or `List<List<Double>>` with corresponding static functions.

To multiply matrices, use `times` function or equivalent operator `*`.

### Example

    val m1 = Matrix.from2dArray(arrayOf(
        arrayOf(1.0, 2.0, -3.0),
        arrayOf(0.1, 0.02, 0.003)
    ))
    val m2 = Matrix.from2dArray(arrayOf(
        arrayOf(100.0, 1.0),
        arrayOf(1.0, 1.0),
        arrayOf(0.01, 1.0)
    ))
    (m1 * m2).print()

## Algorithm

Strassen algorithm of matrix multiplication is used to achieve asymptotic time better than O(n^3). 
This algorithm splits squared matrix of size (2^n x 2^n) on 4 submatrices, represents multiplication as combination of 8 multiplications of submatrices (called naive algorithm)
and then reorganizes submatrices to represent the same multiplication as combination of 7 multiplications of submatrices instead of 8.

For squared matrices of different sizes and rectangular matrices instead of making them bigger and fill extra space with zeros, 
I split it on unequal sized submatrices, leaving size of top left submatrix same as in Strassen algorithm,
and make all operations size-safed to create extra space only where it neccessary for calculations.

When matrix is too thin so empty submatrix appears after splitting, Strassen algorithm is still uses 7 multiplications, because empty submatrices adds to non-empty ones. 
But before reorganization, when there are 8 multiplications of submatrices, empty submatrix multiplies with other and this multiplication is skipped.
So in cases where empty submatrix appears, I use naive algorithm to optimize execution time.

Althouth Strassen algorithm has better complexity, it loses on small matrices. 
So for matrices with size of side less then 128 the simple algorithm of 3 nested for-loops is used.

### Time and space optimizations

* Strassen algorithm, that has complexity O(n^2.81), which is better, then trivial O(n^3) algorithm  _(time optimization)_
* Size-safed operations to avoid extending matrix up to squared 2^n size  _(space optimization)_
* Naive recursive algorithm on thin matrices  _(time optimization, space optimization)_
* 3 nested for-loops algorithm for small matrices, including the end of Strassen and naive recursive algorithms  _(time optimization, space optimization)_
* Cache friendly traverse of matrices in for-loops  _(time optimization)_

### Possible improvements

* Branches of Strassen algorithm and Naive recursive algorithm can be simply parallelized
* Class can be easily changed to Generic one to content not only doubles

## Stats

TODO
