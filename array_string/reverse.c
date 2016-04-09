#include <stdio.h>
#include <string.h>

/**
 * Cracking the Coding Interview(5ed) Problem 1.2:
 * Implement a function reverse(char* str) in C/C++ which reverses a
 * null-terminated string.
 */
void reverse(const char* in, char* out) {
    int len = strlen(in);
    const char* p = in;
    char* q = out + len;
    *q = 0;
    --q;
    for (; *p; ++p, --q) {
        *q = *p;
    }
}

void reverse_in_place(char* str) {
    if (!str) return;

    char* end = str;
    char tmp;
    for (; *end; ++end) {
    }
    for (--end; str < end; ++str, --end) {
        tmp = *str;
        *str = *end;
        *end = tmp;
    }
}

int main(int argc, char* argv[]) {
    // char *str = argv[1];
    // WRONG! char *str = "this is a test";
    char str[] = "this is a test!";
    char str2[100];
    printf("original str=%s\n", str);
    reverse(str, str2);
    printf("reversed(out of place)=%s\n", str2);
    printf("original str=%s\n", str);
    reverse_in_place(str);
    printf("reversed(in place)=%s\n", str);
    return 0;
}
