#include <iostream>
#include <stdexcept>

#include "Bitset.h"

using namespace std;

template<size_t SIZE>
Bitset<SIZE>::Bitset() {
    clear();
}

static bool allSet(int n, int start, int end) {
    const size_t UNIT = sizeof(int) * 8;
    n >>= (UNIT - 1 - end); // move to the rightmost
    int rightmost0 = ~n & (n + 1); // find rightmost 0-bit
    return rightmost0 == 0 || rightmost0 >= (1 << (end - start + 1));
}

static int bitPosition(int idx) {
    const size_t UNIT = sizeof(int) * 8;
    return UNIT - idx % UNIT - 1;
}

template<size_t SIZE>
void Bitset<SIZE>::checkIndex(size_t idx) const {
    if (idx >= SIZE) {
        throw std::invalid_argument("index " + to_string(idx) + " is too large");
    }
}

template<size_t SIZE>
void Bitset<SIZE>::set(size_t idx) {
    checkIndex(idx);

    data[idx / UNIT] |= (1 << bitPosition(idx));
}

template<size_t SIZE>
void Bitset<SIZE>::reset(size_t idx) {
    checkIndex(idx);

    data[idx / UNIT] &= ~(1 << bitPosition(idx));
}

template<size_t SIZE>
bool Bitset<SIZE>::isSet(size_t idx) const {
    checkIndex(idx);

    return data[idx / UNIT] & (1 << bitPosition(idx));
}

template<size_t SIZE>
bool Bitset<SIZE>::isRangeSet(size_t start_idx, size_t end_idx) const {
    checkIndex(start_idx);
    checkIndex(end_idx);
    if (end_idx < start_idx) {
        return true; // or false depending on the specification
    }
    int startChunk = start_idx / UNIT;
    int endChunk = end_idx / UNIT;
    if (startChunk == endChunk) {
        return allSet(data[startChunk], start_idx, end_idx);
    }

    if (!allSet(data[startChunk], start_idx, UNIT - 1)) { return false; }

    if (!allSet(data[endChunk], 0, end_idx)) { return false; }

    for (int i = startChunk + 1; i < endChunk; i++) {
        if (data[i] + 1 != 0) { return false; }
    }
    return true;
}

template<size_t SIZE>
void Bitset<SIZE>::clear() {
    memset(data, 0, sizeof(data));
}

template<size_t SIZE>
ostream& operator<<(ostream& os, const Bitset<SIZE>& bitSet) {
    for (int i = 0; i < SIZE; i++) {
        os << (bitSet.isSet(i) ? 1 : 0);
    }
    return os;
}

template<size_t SIZE>
void test() {
    Bitset<SIZE> bs;
    cout << "initial bs=" << bs << endl;
    bs.set(3);
    cout << "after set 3 bs=" << bs << endl;
    bs.set(5);
    cout << "after set 5 bs=" << bs << endl;
    cout << "from 3 to 5: isRangeSet " << bs.isRangeSet(3, 5) << endl;
    bs.set(4);
    cout << "after set 4 bs=" << bs << endl;
    cout << "from 3 to 5: isRangeSet " << bs.isRangeSet(3, 5) << endl;
    bs.reset(3);
    cout << "after reset 3 bs=" << bs << endl;
    bs.set(SIZE - 1);
    cout << "after set " << (SIZE-1) << " bs=" << bs << endl;
    bs.clear();
    cout << "after clear; bs=" << bs << endl;
}

int main() {
   test<7>();
    test<8>();
    test<9>();


    Bitset<65> bs;
    cout << "initial bs=" << bs << endl;
    for (int i = 5; i < 63; i++) {
        cout << "setting " << i << endl;
        bs.set(i);
        // use assertion!
        cout << "from 6 to 35: isRangeSet " << bs.isRangeSet(6, 35) << endl;
    }
    cout << "reset 33" << endl;
    bs.reset(33);
    cout << "from 6 to 35: isRangeSet should be false: " << bs.isRangeSet(6, 35) << endl;
    cout << "from 6 to 33: isRangeSet should be false: " << bs.isRangeSet(6, 33) << endl;
    cout << "from 6 to 32: isRangeSet should be true: " << bs.isRangeSet(6, 32) << endl;
    try {
        bs.reset(73);
    } catch(exception& e) {
        cout << e.what() << endl;
    }
}
