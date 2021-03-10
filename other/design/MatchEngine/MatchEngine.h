#include <vector>
#include <unordered_map>
#include <set>

//  Please implement a matching engine with the following methods:

// - void addOrderAndMatch(uint64_t order_id, Side side, uint64_t price, uint64_t qty, std::vector<const OrderExec*>* executions_out)
// The content of the OrderExec class is up to you. Side is simply an enum with Buy and Sell values. This method takes in a new Order
// and adds it to the book. If adding it to the book results in matches, the executions should be appended to the executions_out
// vector. The ownership of OrderExec appended in executions_out is kept by the matching engine.

// - void modifyOrder(uint64_t order_id, qty_t new_qty)
// This method modifies the quantity of an existing order.

// - void removeOrder(uint64_t order_id)
// This method removes an existing order.

// Assume that the priority of orders is FIFO. We want all of the methods above to execute with the lowest latency possible.

enum Side { BUY, SELL };

class Order {
    private:
        uint64_t orderId;
        Side side;
        uint64_t price;
        uint64_t qty;

    public:
        Order(uint64_t order_id, Side side, uint64_t price, uint64_t qty) : orderId(order_id), side(side), price(price), qty(qty) {}
        uint64_t getOrderId() const { return orderId; }
        uint64_t getPrice() const { return price; }
        uint64_t getQuantity() const { return qty; }
        void setQuantity(uint64_t qty) { this->qty = qty; }
        Side getSide() const { return side; }

        bool operator<(const Order& other) const;
        friend std::ostream& operator<<(std::ostream&, const Order&);
};


class OrderExec {
    private:
        static uint64_t nextExecId;

        uint64_t execId;
        Order* buy;
        Order* sell;
        uint64_t price; // deal price
        uint64_t qty;

    public:
        OrderExec(Order* buy, Order* sell, uint64_t price, uint64_t qty);

        friend std::ostream& operator<<(std::ostream&, const OrderExec&);
};

class MatchEngine {
    private:
        std::unordered_map<int, Order*> orders;
        std::set<Order*, std::function<bool(const Order*, const Order*)>> sellSet;
        std::set<Order*, std::function<bool(const Order*, const Order*)>> buySet;

        Order* createOrder(uint64_t order_id, Side side, uint64_t price, uint64_t qty);

    public:
        MatchEngine();
        ~MatchEngine();
        MatchEngine(const MatchEngine&) = delete;
        MatchEngine& operator=(const MatchEngine&) = delete;

        void addOrderAndMatch(uint64_t order_id, Side side, uint64_t price, uint64_t qty, std::vector<const OrderExec*>* executions_out);
        void modifyOrder(uint64_t order_id, uint64_t new_qty);
        void removeOrder(uint64_t order_id);
};

