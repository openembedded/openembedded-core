SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://0001-Unset-need_charset_alias-when-building-for-musl.patch \
           file://0002-improve-reproducibility.patch \
          "

SRC_URI[md5sum] = "caabf9727fa429626316619a6369fffa"
SRC_URI[sha256sum] = "4f4a673b6d466efa50fbfba796bd84a46ae24e370fa562ede5b21ab53c11a920"

require wget.inc
