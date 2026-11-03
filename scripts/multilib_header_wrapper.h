/*
 * Copyright (C) 2005-2011 by Wind River Systems, Inc.
 *
 * SPDX-License-Identifier: MIT
 * 
 */

#ifndef __MHWORDSIZE
# if defined(__has_include)
#  if __has_include(<bits/alltypes-32.h>) && !__has_include(<bits/alltypes-64.h>)
#   define __MHWORDSIZE 32				/* musl, 32-bit only */
#  elif __has_include(<bits/alltypes-64.h>) && !__has_include(<bits/alltypes-32.h>)
#   define __MHWORDSIZE 64				/* musl, 64-bit only */
#  elif __has_include(<bits/alltypes.h>)
#   define __MHWORDSIZE (__SIZEOF_POINTER__ * 8)	/* musl, both (SDK) */
#  endif
# endif
# ifndef __MHWORDSIZE
#  include <bits/wordsize.h>				/* glibc */
#  define __MHWORDSIZE __WORDSIZE
# endif
#endif

#if __MHWORDSIZE == 32

#ifdef _MIPS_SIM

#if _MIPS_SIM == _ABIO32
#include <ENTER_HEADER_FILENAME_HERE-32.h>
#elif _MIPS_SIM == _ABIN32
#include <ENTER_HEADER_FILENAME_HERE-n32.h>
#else
#error "Unknown _MIPS_SIM"
#endif

#else /* _MIPS_SIM is not defined */
#include <ENTER_HEADER_FILENAME_HERE-32.h>
#endif

#elif __MHWORDSIZE == 64
#include <ENTER_HEADER_FILENAME_HERE-64.h>
#else
#error "Unknown __WORDSIZE detected"
#endif /* matches #if __MHWORDSIZE == 32 */
  
