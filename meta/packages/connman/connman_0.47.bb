require connman.inc
PV       = "0.47"
PR = "r1"

SRC_URI  = "http://www.kernel.org/pub/linux/network/connman/${P}.tar.bz2 \
            file://dbusperms.patch \
            file://connman "

