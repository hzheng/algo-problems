#include <iostream>
#include <stdexcept>

#include "MatchEngine.h"

using namespace std;

bool Order::operator<(const Order& other) const {
    if (price == other.price) {
        return orderId < other.orderId;
    }

    return (side == BUY) ? (price > other.price) : (price < other.price);
}

ostream& operator<<(ostream& os, const Order& order) {
    os << ((order.side == BUY) ? "B" : "S") <<"; id=" << order.orderId << ";prices=" << order.price << ";qty=" << order.qty;
    return os;
}

uint64_t OrderExec::nextExecId = 0;

OrderExec::OrderExec(Order* buy, Order* sell, uint64_t price, uint64_t qty) : buy(buy), sell(sell), price(price), qty(qty) {
    execId = nextExecId++;
}

ostream& operator<<(ostream& os, const OrderExec& orderExec) {
    os << "execId=" << orderExec.execId << "; price=" << orderExec.price << ";qty=" << orderExec.qty
        << "; buy=[" << *orderExec.buy << "]; sell=[" << *orderExec.sell << "]";
    return os;
}

static bool compareOrder(const Order* a, const Order* b) {
    return *a < *b;
};

MatchEngine::MatchEngine()
    : sellSet(set<Order*, function<bool(const Order*, const Order*)>>(compareOrder)),
    buySet(set<Order*, function<bool(const Order*, const Order*)>>(compareOrder))
{}

MatchEngine::~MatchEngine() {
    for (auto it = orders.begin(); it != orders.end(); it++) {
        delete it->second;
    }
}

Order* MatchEngine::createOrder(uint64_t order_id, Side side, uint64_t price, uint64_t qty) {
    // TODO: create order from pool for time and memeory efficiency
    Order* order = new Order(order_id, side, price, qty);
    orders[order_id] = order;
    return order;
}

void MatchEngine::addOrderAndMatch(uint64_t order_id, Side side, uint64_t price, uint64_t qty, vector<const OrderExec*>* executions_out) {
    Order* order = createOrder(order_id, side, price, qty);
    cout << "++++ " << *order << endl;
    (side == BUY ? buySet : sellSet).insert(order);

    while (!buySet.empty() && !sellSet.empty()) {
        auto buyItr = buySet.begin();
        auto sellItr = sellSet.begin();
        auto buy = *buyItr;
        auto sell = *sellItr;
        uint64_t sPrice = sell->getPrice();
        uint64_t bPrice = buy->getPrice();
        cout << "matching buy: [" << *buy << "] & sell: ["<< *sell << "]" << endl;
        if (bPrice < sPrice) { break; }

        uint64_t sQty = sell->getQuantity();
        uint64_t bQty = buy->getQuantity();
        uint64_t dealQty = min(sQty, bQty);
        uint64_t dealPrice = (sPrice + bPrice) / 2;
        // TODO: create OrderExec from pool for time and memeory efficiency(and avoid memory leak)
        OrderExec* orderExec = new OrderExec(buy, sell, dealPrice, dealQty);
        executions_out->push_back(orderExec);
        buySet.erase(buy);
        sellSet.erase(sell);
        if (sQty > dealQty) {
            sellSet.insert(createOrder(sell->getOrderId(), SELL, sell->getPrice(), sQty - dealQty));
        } else if (bQty > dealQty) {
            buySet.insert(createOrder(buy->getOrderId(), BUY, buy->getPrice(), bQty - dealQty));
        }
    }
}

void MatchEngine::modifyOrder(uint64_t order_id, uint64_t new_qty) {
    Order* order = orders[order_id];
    if (order) {
        order->setQuantity(new_qty);
    }
}

 void MatchEngine::removeOrder(uint64_t order_id) {
     Order* order = orders[order_id];
     if (order) {
         (order->getSide() == BUY ? buySet : sellSet).erase(order);
     }
 }

static uint64_t orderId = 0;

void add(MatchEngine& engine, vector<const OrderExec*>& executions, Side side, uint64_t price, uint64_t qty) {
    engine.addOrderAndMatch(orderId++, side, price, qty, &executions);
    if (executions.empty()) {
        cout << "-----no exec" << endl;
        return;
    }
    cout << "----order exec---" << endl;
    for (auto orderExec : executions) {
       cout << *orderExec << endl;
    }
}

void test1() {
    MatchEngine engine;
    vector<const OrderExec*> executions;
    add(engine, executions, BUY, 12, 15);
    add(engine, executions, SELL, 13, 3);
    add(engine, executions, SELL, 11, 2);
    add(engine, executions, SELL, 10, 1);
    //engine.removeOrder(0);
    add(engine, executions, SELL, 12, 3);
    add(engine, executions, SELL, 14, 3);
    engine.modifyOrder(1, 10);
    //engine.removeOrder(1);
    engine.removeOrder(5);
    add(engine, executions, BUY, 15, 25);
}

int main() {
    test1();
    return 0;
}


