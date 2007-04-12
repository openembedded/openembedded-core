SRC_URI = "http://www.opensync.org/attachment/wiki/download/msynctool-0.22.tar.bz2?format=raw"

LICENSE = "GPL"
DEPENDS = "libopensync"
HOMEPAGE = "http://www.opensync.org/"

inherit autotools pkgconfig

require opensync-unpack.inc
