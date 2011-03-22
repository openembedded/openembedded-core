DESCRIPTION = "GMP is a free library for arbitrary precision arithmetic, operating on signed integers, rational numbers, and floating point numbers"
HOMEPAGE = "http://gmplib.org/"
BUGTRACKER = "http://gmplib.org/mailman/listinfo/gmp-bugs"
# demo is licensed under GPLv2+, which however we don't package at all
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a \
                    file://COPYING.LIB;md5=fbc093901857fcd118f065f900982c24 \
                    file://gmp-h.in;startline=6;endline=21;md5=5e25ffd16996faba8c1cd27b04b16099"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/gmp/${BP}.tar.bz2 \
           file://disable-stdc.patch"

inherit autotools
