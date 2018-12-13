require qemu.inc

inherit ptest

RDEPENDS_${PN}-ptest = "bash make"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI = "https://download.qemu.org/${BPN}-${PV}.tar.xz \
           file://powerpc_rom.bin \
           file://0001-sdl.c-allow-user-to-disable-pointer-grabs.patch \
           file://0002-qemu-Add-missing-wacom-HID-descriptor.patch \
           file://0003-Add-subpackage-ptest-which-runs-all-unit-test-cases-.patch \
           file://run-ptest \
           file://0004-qemu-Add-addition-environment-space-to-boot-loader-q.patch \
           file://0005-qemu-disable-Valgrind.patch \
           file://0006-qemu-Limit-paths-searched-during-user-mode-emulation.patch \
           file://0007-qemu-native-set-ld.bfd-fix-cflags-and-set-some-envir.patch \
           file://0008-chardev-connect-socket-to-a-spawned-command.patch \
           file://0009-apic-fixup-fallthrough-to-PIC.patch \
           file://0010-linux-user-Fix-webkitgtk-hangs-on-32-bit-x86-target.patch \
           file://0011-Revert-linux-user-fix-mmap-munmap-mprotect-mremap-sh.patch \
           "
UPSTREAM_CHECK_REGEX = "qemu-(?P<pver>\d+(\.\d+)+)\.tar"

SRC_URI_append_class-native = " \
            file://0012-fix-libcap-header-issue-on-some-distro.patch \
            file://0013-cpus.c-Add-error-messages-when-qemi_cpu_kick_thread-.patch \
            "

SRC_URI[md5sum] = "fb687ce0b02d3bf4327e36d3b99427a8"
SRC_URI[sha256sum] = "6a0508df079a0a33c2487ca936a56c12122f105b8a96a44374704bef6c69abfc"

COMPATIBLE_HOST_mipsarchn32 = "null"
COMPATIBLE_HOST_mipsarchn64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
    install -Dm 0755 ${WORKDIR}/powerpc_rom.bin ${D}${datadir}/qemu
}

do_compile_ptest() {
	make buildtest-TESTS
}

do_install_ptest() {
	cp -rL ${B}/tests ${D}${PTEST_PATH}
	find ${D}${PTEST_PATH}/tests -type f -name "*.[Sshcod]" | xargs -i rm -rf {}

	cp ${S}/tests/Makefile.include ${D}${PTEST_PATH}/tests
	# Don't check the file genreated by configure
	sed -i -e '/wildcard config-host.mak/d' \
	       -e '$ {/endif/d}' ${D}${PTEST_PATH}/tests/Makefile.include
}
