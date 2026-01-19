# Settings for the vulns git repository configuration
IMPROVE_KERNEL_CVE_SRC_URI ?= "git://git.kernel.org/pub/scm/linux/security/vulns.git;branch=master;protocol=https"
IMPROVE_KERNEL_CVE_SRCREV ?= "${@bb.fetch2.get_autorev(d)}"
IMPROVE_KERNEL_CVE_NETWORK ?= "1"
IMPROVE_KERNEL_CVE_WORKDIR ?= "${WORKDIR}/vulns"
IMPROVE_KERNEL_CVE_DESTSUFFIX ?= "git"
IMPROVE_KERNEL_CVE_UNPACK_DIR ?= "${IMPROVE_KERNEL_CVE_WORKDIR}/${IMPROVE_KERNEL_CVE_DESTSUFFIX}"

# Settings for SPDX support
IMPROVE_KERNEL_PREFERRED_PROVIDER ?= ""
IMPROVE_KERNEL_SPDX_FILE ?= ""

python __anonymous() {
    srcrev = d.getVar("IMPROVE_KERNEL_CVE_SRCREV", True) or ""
    network = d.getVar("IMPROVE_KERNEL_CVE_NETWORK", True) or "0"
    # Check the IMPROVE_KERNEL_SPDX_FILE variable was set
    if not d.getVar("IMPROVE_KERNEL_SPDX_FILE"):
        bb.fatal("improve_kernel_cve: IMPROVE_KERNEL_SPDX_FILE is not set. Need to inherit improve_kernel_cve_report-spdx-2.2 or improve_kernel_cve_report-spdx")
        return
    # Check if networking is enabled to set SRC_URI
    if network == "0":
        d.appendVar("SRC_URI", " ${IMPROVE_KERNEL_CVE_SRC_URI};name=improve-kernel-cve;destsuffix=${IMPROVE_KERNEL_CVE_DESTSUFFIX}")
    # Check offline mode with AUTOREV-like SRCREV
    if network == "0" and srcrev.strip() in ("${AUTOREV}", "AUTOINC", "INVALID"):
        bb.fatal("improve_kernel_cve: Offline mode but SRCREV is set to AUTOREV/AUTOINC/INVALID. Cannot proceed without network access or use a fixed SRCREV.")
    d.setVar("SRCREV_improve-kernel-cve", d.getVar("IMPROVE_KERNEL_CVE_SRCREV"))
    # Check which SPDX class is inherited
    inherits = (d.getVar("INHERIT") or "")
    if "create-spdx-2.2" in inherits:
        bb.build.addtask("do_scout_extra_kernel_vulns", "do_build", "do_rootfs", d)
    elif "create-spdx" in inherits:
        bb.build.addtask('do_scout_extra_kernel_vulns', 'do_build', 'do_create_image_sbom_spdx', d)
}

python do_clean:append() {
    import os, glob
    deploy_dir = d.expand('${DEPLOY_DIR_IMAGE}')
    for f in glob.glob(os.path.join(deploy_dir, '*scouted.json')):
        bb.note("Removing " + f)
        os.remove(f)
}

python do_clone_kernel_cve() {
    import subprocess
    import shutil, os
    # Check if the system is using SPDX 3.0
    inherit_var = d.getVar("INHERIT")
    preferred_provider = d.getVar("IMPROVE_KERNEL_PREFERRED_PROVIDER")
    if preferred_provider not in inherit_var:
        bb.warn(f"improve_kernel_cve: Requires the class {preferred_provider} enable in INHERIT variable.")
        return
    network_allowed = d.getVar("IMPROVE_KERNEL_CVE_NETWORK") == "1"
    workdir = d.getVar("IMPROVE_KERNEL_CVE_WORKDIR")
    unpack_dir = d.getVar("IMPROVE_KERNEL_CVE_UNPACK_DIR")
    # Remove existing unpacked directory if any
    if os.path.exists(workdir):
        shutil.rmtree(workdir)
    # Prepare fetcher
    src_uri_list = (d.getVar('SRC_URI') or "").split()
    cve_uris = []
    for uri in src_uri_list:
        if "name=improve-kernel-cve" in uri:
            cve_uris.append(uri)
    if not cve_uris:
        bb.note("No CVE exclusions SRC_URI found, skipping fetch")
        return
    fetcher = bb.fetch2.Fetch(cve_uris, d)
    # Clone only if network is allowed
    if network_allowed:
        fetcher.download()
    else:
        # Offline mode without network access
        bb.note("IMPROVE_KERNEL_CVE_NETWORK=0: Skipping online fetch. Checking local downloads in DL_DIR...")
        have_sources = False
        dl_dir = d.getVar("DL_DIR")
        srcrev = d.getVar("SRCREV_improve-kernel-cve")
        bb.note(f"Checking for sources for SRCREV: {srcrev}")
        # Check SRCREV is NOT set to AUTOREV
        if srcrev.strip() in ("${AUTOREV}", "AUTOINC", "INVALID"):
            bb.fatal("improve-kernel-cve: Offline mode but SRCREV is set to AUTOREV/AUTOINC/INVALID. Cannot proceed without network access or use a fixed SRCREV.")
            return
        # Loop through the fetcher's expanded URL data
        for ud in fetcher.expanded_urldata():
            ud.setup_localpath(d)
            # Check mirror tarballs first
            for mirror_fname in ud.mirrortarballs:
                mirror_path = os.path.join(dl_dir, mirror_fname)
                if os.path.exists(mirror_path):
                    bb.note(f"Found mirror tarball: {mirror_path}")
                    have_sources = True
                    break
            # If no mirror, check original download path
            if not have_sources and ud.localpath and os.path.exists(ud.localpath):
                bb.note(f"Found local download: {ud.localpath}")
                have_sources = True
            if not have_sources:
                bb.fatal("improve-kernel-cve: Offline mode but required source is missing.\n"f"SRC_URI = {ud.url}")
                return
    # Unpack into the standard work directory
    fetcher.unpack(unpack_dir)
    # Remove the folder ${PN} set by unpack
    subdirs = [d for d in os.listdir(unpack_dir) if os.path.isdir(os.path.join(unpack_dir, d))]
    if len(subdirs) == 1:
        srcdir = os.path.join(unpack_dir, subdirs[0])
        for f in os.listdir(srcdir):
            shutil.move(os.path.join(srcdir, f), unpack_dir)
        shutil.rmtree(srcdir)
}
do_clone_kernel_cve[network] = "${IMPROVE_KERNEL_CVE_NETWORK}"
do_clone_kernel_cve[nostamp] = "1"
do_clone_kernel_cve[doc] = "Clone the latest kernel vulnerabilities from https://git.kernel.org/pub/scm/linux/security/vulns.git"
addtask clone_kernel_cve after do_fetch before do_scout_extra_kernel_vulns

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
    if [ ! -d "${IMPROVE_KERNEL_CVE_WORKDIR}" ]; then
        bbwarn "improve_kernel_cve: Vulnerabilities data not found in ${IMPROVE_KERNEL_CVE_WORKDIR}."
        return 0
    fi

    #Run the improve_kernel_cve_report.py script
    bbplain "improve_kernel_cve: Using SPDX file for extra kernel vulnerabilities scouting: ${IMPROVE_KERNEL_SPDX_FILE}"
    python3 "${improve_kernel_cve_script}" \
        --spdx "${IMPROVE_KERNEL_SPDX_FILE}" \
        --old-cve-report "${CVE_CHECK_MANIFEST_JSON}" \
        --new-cve-report "${new_cve_report_file}" \
        --datadir "${IMPROVE_KERNEL_CVE_WORKDIR}"
    bbplain "Improve CVE report with extra kernel cves: ${new_cve_report_file}"

    #Create a symlink as every other JSON file in tmp/deploy/images
    ln -sf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.scouted.json ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}${IMAGE_MACHINE_SUFFIX}${IMAGE_NAME_SUFFIX}.scouted.json
}
do_scout_extra_kernel_vulns[nostamp] = "1"
do_scout_extra_kernel_vulns[doc] = "Scout extra kernel vulnerabilities and create a new enhanced version of the cve_check file in the deploy directory"