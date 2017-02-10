SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://0001-Unset-need_charset_alias-when-building-for-musl.patch \
          "

SRC_URI[md5sum] = "f9e928e293e4d7de73ff6f19e0c4b423"
SRC_URI[sha256sum] = "dd9523039845f69e8e945e9f2d5a38af6b1e29efa6ff53830507310235ddade8"

require wget.inc
