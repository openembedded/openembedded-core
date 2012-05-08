lib_LTLIBRARIES = libz.la

libz_la_SOURCES = adler32.c compress.c crc32.c gzlib.c gzclose.c gzread.c \
                  gzwrite.c uncompr.c deflate.c trees.c zutil.c inflate.c \
                  infback.c inftrees.c inffast.c

libz_la_LDFLAGS = -version-number 1:2:5 --version-script zlib.map

include_HEADERS = zconf.h zlib.h zlibdefs.h

pkgconfigdir = $(libdir)/pkgconfig
pkgconfig_DATA = zlib.pc
