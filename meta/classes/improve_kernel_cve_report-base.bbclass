# Settings for SPDX support

# Setting to specify preferred provider for kernel SPDX file ("create-spdx" or "create-spdx-2.2")
IMPROVE_KERNEL_PREFERRED_PROVIDER ?= ""
# Setting to specify the path to the SPDX file to be used for extra kernel vulnerabilities scouting
IMPROVE_KERNEL_SPDX_FILE ?= ""

python __anonymous() {
    if bb.data.inherits_class("create-spdx-2.2", d):
        if not d.getVar("IMPROVE_KERNEL_PREFERRED_PROVIDER") == "create-spdx-2.2":
            bb.fatal("improve_kernel_cve_report: IMPROVE_KERNEL_PREFERRED_PROVIDER is set to '%s', but 'create-spdx-2.2' class is inherited. Please check your configuration." % d.getVar("IMPROVE_KERNEL_PREFERRED_PROVIDER"))
        bb.build.addtask("do_scout_extra_kernel_vulns", "do_build", "do_rootfs", d)
    elif bb.data.inherits_class("create-spdx", d):
        if not d.getVar("IMPROVE_KERNEL_PREFERRED_PROVIDER") == "create-spdx":
            bb.fatal("improve_kernel_cve_report: IMPROVE_KERNEL_PREFERRED_PROVIDER is set to '%s', but 'create-spdx' class is inherited. Please check your configuration." % d.getVar("IMPROVE_KERNEL_PREFERRED_PROVIDER"))
        bb.build.addtask('do_scout_extra_kernel_vulns', 'do_build', 'do_create_image_sbom_spdx', d)
}

python do_clean:append() {
    import os, glob
    deploy_dir = d.expand('${DEPLOY_DIR_IMAGE}')
    for f in glob.glob(os.path.join(deploy_dir, '*scouted.json')):
        bb.note("Removing " + f)
        os.remove(f)
}

do_scout_extra_kernel_vulns() {
    new_cve_report_file="${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.scouted.json"
    improve_kernel_cve_script="${COREBASE}/scripts/contrib/improve_kernel_cve_report.py"

    # Check that IMPROVE_KERNEL_SPDX_FILE is set and the file exists
    if [ -z "${IMPROVE_KERNEL_SPDX_FILE}" ] || [ ! -f "${IMPROVE_KERNEL_SPDX_FILE}" ]; then
        bbwarn "improve_kernel_cve: IMPROVE_KERNEL_SPDX_FILE is empty or file not found: ${IMPROVE_KERNEL_SPDX_FILE}"
        return 0
    fi
    if [ ! -f "${CVE_CHECK_MANIFEST_JSON}" ]; then
        bbwarn "improve_kernel_cve: CVE_CHECK file not found: ${CVE_CHECK_MANIFEST_JSON}. Skipping extra kernel vulnerabilities scouting."
        return 0
    fi
    if [ ! -f "${improve_kernel_cve_script}" ]; then
        bbwarn "improve_kernel_cve: improve_kernel_cve_report.py not found in ${COREBASE}."
        return 0
    fi
    if [ ! -d "${STAGING_DATADIR_NATIVE}/vulns-native" ]; then
        bbwarn "improve_kernel_cve: Vulnerabilities data not found in ${STAGING_DATADIR_NATIVE}/vulns-native."
        return 0
    fi

    #Run the improve_kernel_cve_report.py script
    bbplain "improve_kernel_cve: Using SPDX file for extra kernel vulnerabilities scouting: ${IMPROVE_KERNEL_SPDX_FILE}"
    python3 "${improve_kernel_cve_script}" \
        --spdx "${IMPROVE_KERNEL_SPDX_FILE}" \
        --old-cve-report "${CVE_CHECK_MANIFEST_JSON}" \
        --new-cve-report "${new_cve_report_file}" \
        --datadir "${STAGING_DATADIR_NATIVE}/vulns-native"
    bbplain "Improve CVE report with extra kernel cves: ${new_cve_report_file}"

    #Create a symlink as every other JSON file in tmp/deploy/images
    ln -sf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.scouted.json ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}${IMAGE_MACHINE_SUFFIX}${IMAGE_NAME_SUFFIX}.scouted.json
}
do_scout_extra_kernel_vulns[depends] += "vulns-native:do_populate_sysroot"
do_scout_extra_kernel_vulns[nostamp] = "1"
do_scout_extra_kernel_vulns[doc] = "Scout extra kernel vulnerabilities and create a new enhanced version of the cve_check file in the deploy directory"
addtask scout_extra_kernel_vulnsate_cve_exclusions after do_prepare_recipe_sysroot