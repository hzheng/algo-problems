// Please implement a Bitset class emulating an array of booleans, to be as fast as possible.
// The Bitset class should have a fixed size set at construction time, and offer the following methods:
//
// - A void set(size_t idx) method, which sets the bit (set to 1) at the provided index.
//
// - A void reset(size_t idx) method, which resets the bit (set to 0) at the provided index.
//
// - A bool isSet(size_t idx) method which returns true if the bit at the provided index is set.
//
// - A bool isRangeSet(size_t start_idx, size_t end_idx) method which returns true if all the bits from start_idx to end_idx (all included) are set.
//
// - A void clear() method, which resets all the bits.
template<size_t SIZE>
class Bitset {
    private:
        static const size_t UNIT = sizeof(int) * 8;

        unsigned int data[(SIZE + UNIT - 1) / UNIT];

        void checkIndex(size_t idx) const;

    public:
        Bitset();
        void set(size_t idx);
        void reset(size_t idx);
        bool isSet(size_t idx) const;
        bool isRangeSet(size_t start_idx, size_t end_idx) const;
        void clear();
};

