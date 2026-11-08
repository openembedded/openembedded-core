SUMMARY = "Target packages for the clang SDK"

inherit packagegroup

RDEPENDS:${PN} = " \
    clang \
    clang-format \
    clang-tidy \
    compiler-rt-dev \
    compiler-rt-staticdev \
    libcxx-dev \
    libcxx-staticdev \
"
