SUMMARY = "Target packages for the standalone SDK"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    libgcc \
    libgcc-dev \
    libatomic \
    libatomic-dev \
    libstdc++ \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    ${@bb.utils.contains_any('TC_CXX_RUNTIME', 'llvm android', 'libcxx-dev \
    libcxx-staticdev \
    compiler-rt-dev \
    compiler-rt-staticdev \
    ', '', d)} \
    "

RRECOMMENDS:${PN}:mingw32 = "\
    libssp \
    libssp-dev \
    "
