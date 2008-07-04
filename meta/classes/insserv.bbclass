do_rootfs[depends] += "insserv-native:do_populate_staging"
run_insserv () {
            insserv -p ${IMAGE_ROOTFS}/etc/init.d -c ${STAGING_ETCDIR_NATIVE}/insserv.conf
}
ROOTFS_POSTPROCESS_COMMAND += " run_insserv ; "
