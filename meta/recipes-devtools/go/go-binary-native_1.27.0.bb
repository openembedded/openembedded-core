# This recipe is for bootstrapping our go-cross from a prebuilt binary of Go from golang.org.

SUMMARY = "Go programming language compiler (upstream binary for bootstrap)"
HOMEPAGE = "http://golang.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7998cb338f82d15c0eff93b7004d272a"

PROVIDES = "go-native"

# Checksums available at https://go.dev/dl/
SRC_URI = "https://dl.google.com/go/go${PV}.${BUILD_GOOS}-${BUILD_GOARCH}.tar.gz;name=go_${BUILD_GOTUPLE}"
SRC_URI[go_linux_amd64.sha256sum] = "675c26c449cbb18fc24b74650de1eabbae6e16f64326fd85a283fb3b58280685"
SRC_URI[go_linux_arm64.sha256sum] = "51798d2c42d0e1c6ed7fd9f48728b4193abac9e8aad6dbac2fe96a81f5909bda"
SRC_URI[go_linux_ppc64le.sha256sum] = "068085e257a6014c036c723ca62943edbf48fd168b8d00a6d39527d10b656255"

UPSTREAM_CHECK_URI = "https://golang.org/dl/"
UPSTREAM_CHECK_REGEX = "go(?P<pver>\d+(\.\d+)+)\.linux"

CVE_PRODUCT = "golang:go"
CVE_STATUS[CVE-2024-3566] = "not-applicable-platform: Issue only applies on Windows"
CVE_STATUS[CVE-2024-24786] = "cpe-incorrect: this CVE is for golang-google-protobuf"

S = "${UNPACKDIR}/go"

inherit goarch native

do_compile() {
    :
}

make_wrapper() {
	rm -f ${D}${bindir}/$1
	cat <<END >${D}${bindir}/$1
#!/bin/bash
here=\`dirname \$0\`
export GOROOT="${GOROOT:-\`readlink -f \$here/../lib/go\`}"
\$here/../lib/go/bin/$1 "\$@"
END
	chmod +x ${D}${bindir}/$1
}

do_install() {
    find ${S} -depth -type d -name testdata -exec rm -rf {} +

	install -d ${D}${bindir} ${D}${libdir}/go
	cp --preserve=mode,timestamps -R ${S}/ ${D}${libdir}/

	for f in ${S}/bin/*
	do
	  	make_wrapper `basename $f`
	done
}
