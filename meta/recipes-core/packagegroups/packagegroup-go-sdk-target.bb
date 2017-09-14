SUMMARY = "Target packages for the Go SDK"

inherit packagegroup

RDEPENDS_${PN} = " \
    go-runtime \
    go-runtime-dev \
"
