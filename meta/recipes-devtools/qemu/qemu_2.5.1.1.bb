require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
            file://no-valgrind.patch \
            file://CVE-2016-2198.patch \
            file://pathlimit.patch \
            file://rng_move_request_from_RngEgd_to_RngBackend.patch \
            file://rng_remove_the_unused_request_cancellation_code.patch \
            file://rng_move_request_queue_cleanup_from_RngEgd_to_RngBackend.patch \
            file://CVE-2016-2858.patch \
           "
SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BP}.tar.bz2"
SRC_URI[md5sum] = "f5ff0e71398b9e428b4f177001ba4285"
SRC_URI[sha256sum] = "28d9946e43765a44ccccca3cba5f4f9034f2759ec1f2ce16594ddb6776c8efe6"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
