require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.bz2 \
  file://ofono \
"
SRC_URI[md5sum] = "d863b2b650a525cf75056085398bc8ef"
SRC_URI[sha256sum] = "63e38ea1cf35b00ecbab7611c6caa2adcd33eb10495f7a9f72013d4ab7a14c98"


EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"

