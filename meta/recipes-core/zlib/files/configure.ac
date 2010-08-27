AC_INIT(zlib,1.2.5)
AC_CONFIG_SRCDIR(adler32.c)
AM_INIT_AUTOMAKE(zlibs,1.2.5)

AC_PREREQ([2.59])

AC_PROG_CC([gcc])
AC_PROG_LIBTOOL

AC_HEADER_STDC

zlib_save_CPPFLAGS=$CPPFLAGS
CPPFLAGS="$CPPFLAGS -D_LARGEFILE64_SOURCE"
AC_CHECK_TYPES(off64_t)
CPPFLAGS=$zlib_save_CPPFLAGS

AC_CACHE_CHECK([whether to enable -D_LARGEFILE64_SOURCE], [zlib_cv_use_lfs64], [
    zlib_cv_use_lfs64=no
    if test "$ac_cv_type_off64_t" = "yes"; then
        zlib_cv_use_lfs64=yes
    fi
])

if test "$zlib_cv_use_lfs64" = "yes"; then
    CPPFLAGS="$CPPFLAGS -D_LARGEFILE64_SOURCE"

    #APR_ADDTO(CPPFLAGS, [-D_LARGEFILE64_SOURCE])
fi

cat > zlibdefs.h << EOF
/* zlibdefs.h -- compile-time definitions for the zlib compression library
 * Copyright (C) 1995-2006 Jean-loup Gailly.
 * For conditions of distribution and use, see copyright notice in zlib.h
 */

#include <sys/types.h>	/* for off_t */
#include <unistd.h>	/* for SEEK_* and off_t */
#ifdef VMS
#  include <unixio.h>	/* for off_t */
#endif
#ifndef z_off_t
#  define z_off_t off_t
#endif
EOF

AC_CONFIG_FILES([Makefile])

AC_OUTPUT
