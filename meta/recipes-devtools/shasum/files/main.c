#include <stdio.h>
#include <stdlib.h>

#include "mhash_sha256.h"

/*
 * from driver.c of mhash
 */
static const char hexconvtab[] = "0123456789abcdef";

static char *
bin2hex(const unsigned char *old, const size_t oldlen, size_t * newlen)
{
    unsigned char *new = NULL;
    int i, j;

    new = (char *) malloc(oldlen * 2 * sizeof(char) + 1);
    if (!new)
        return (new);

    for (i = j = 0; i < oldlen; i++) {
        new[j++] = hexconvtab[old[i] >> 4];
        new[j++] = hexconvtab[old[i] & 15];
    }
    new[j] = '\0';

    if (newlen)
        *newlen = oldlen * 2 * sizeof(char);

    return (new);
}


int main(int argc, char** argv)
{
    FILE *file;
    size_t n;
    SHA256_CTX ctx;
    unsigned char buf[1024];
    byte output[33];

    if ( argc <= 1 ) {
        return EXIT_FAILURE;
    }

    if ( (file=fopen(argv[1], "rb")) == NULL ) {
        return EXIT_FAILURE;
    }

    sha256_init(&ctx);

    while ( (n=fread( buf, 1, sizeof(buf), file)) > 0 )
        sha256_update(&ctx, buf, n );

    sha256_final(&ctx);
    sha256_digest(&ctx, output);

    printf("%s ?%s\n", bin2hex(output, 32, &n), argv[1]);
    return EXIT_SUCCESS;
}
