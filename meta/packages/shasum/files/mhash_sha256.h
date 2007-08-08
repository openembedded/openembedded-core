/* sha.h
 *
 * The sha1 and sha256 hash functions.
 */

/* nettle, low-level cryptographics library
 *
 * Copyright (C) 2001 Niels Möller
 *  
 * The nettle library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * The nettle library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with the nettle library; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA.
 */
 
#ifndef NETTLE_SHA_H_INCLUDED
#define NETTLE_SHA_H_INCLUDED

#include <inttypes.h>

typedef uint32_t word32;
typedef unsigned char byte;


/* SHA256 */

#define SHA256_DIGEST_SIZE 32
#define SHA256_DATA_SIZE 64

/* Digest is kept internally as 8 32-bit words. */
#define _SHA256_DIGEST_LENGTH 8

typedef struct sha256_ctx
{
  word32 state[_SHA256_DIGEST_LENGTH];    /* State variables */
  word32 count_low, count_high;           /* 64-bit block count */
  byte block[SHA256_DATA_SIZE];          /* SHA256 data buffer */
  unsigned int index;                       /* index into buffer */
} SHA256_CTX;

void
sha256_init(struct sha256_ctx *ctx);

void
sha256_update(struct sha256_ctx *ctx, const byte *data, unsigned length);

void
sha256_final(struct sha256_ctx *ctx);

void
sha256_digest(const struct sha256_ctx *ctx, byte *digest);


#endif /* NETTLE_SHA_H_INCLUDED */
