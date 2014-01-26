from oe.utils import execute_pre_post_process
import os
import subprocess
import multiprocessing


def generate_image(arg):
    (type, subimages, create_img_cmd) = arg

    bb.note("Running image creation script for %s: %s ..." %
            (type, create_img_cmd))

    try:
        subprocess.check_output(create_img_cmd)
    except subprocess.CalledProcessError as e:
        return("Error: The image creation script %s returned %d!" %
               (e.cmd, e.returncode))

    return None


class Image(object):
    def __init__(self, d):
        self.d = d

    def _get_rootfs_size(self):
        """compute the rootfs size"""
        rootfs_alignment = int(self.d.getVar('IMAGE_ROOTFS_ALIGNMENT', True))
        overhead_factor = float(self.d.getVar('IMAGE_OVERHEAD_FACTOR', True))
        rootfs_req_size = int(self.d.getVar('IMAGE_ROOTFS_SIZE', True))
        rootfs_extra_space = eval(self.d.getVar('IMAGE_ROOTFS_EXTRA_SPACE', True))

        output = subprocess.check_output(['du', '-ks',
                                          self.d.getVar('IMAGE_ROOTFS', True)])
        size_kb = int(output.split()[0])
        base_size = size_kb * overhead_factor
        if base_size < rootfs_req_size:
            base_size = rootfs_req_size + rootfs_extra_space

        if base_size != int(base_size):
            base_size = int(base_size + 1)

        base_size += rootfs_alignment - 1
        base_size -= base_size % rootfs_alignment

        return base_size

    def _create_symlinks(self, subimages):
        """create symlinks to the newly created image"""
        deploy_dir = self.d.getVar('DEPLOY_DIR_IMAGE', True)
        img_name = self.d.getVar('IMAGE_NAME', True)
        link_name = self.d.getVar('IMAGE_LINK_NAME', True)
        manifest_name = self.d.getVar('IMAGE_MANIFEST', True)

        os.chdir(deploy_dir)

        if link_name is not None:
            for type in subimages:
                if os.path.exists(img_name + ".rootfs." + type):
                    dst = link_name + "." + type
                    src = img_name + ".rootfs." + type
                    bb.note("Creating symlink: %s -> %s" % (dst, src))
                    os.symlink(src, dst)

            if manifest_name is not None and \
                    os.path.exists(manifest_name) and \
                    not os.path.exists(link_name + ".manifest"):
                os.symlink(os.path.basename(manifest_name),
                           link_name + ".manifest")

    def _remove_old_symlinks(self):
        """remove the symlinks to old binaries"""

        if self.d.getVar('IMAGE_LINK_NAME', True):
            deploy_dir = self.d.getVar('DEPLOY_DIR_IMAGE', True)
            for img in os.listdir(deploy_dir):
                if img.find(self.d.getVar('IMAGE_LINK_NAME', True)) == 0:
                    img = os.path.join(deploy_dir, img)
                    if os.path.islink(img):
                        if self.d.getVar('RM_OLD_IMAGE', True) == "1":
                            os.remove(os.path.realpath(img))

                        os.remove(img)

    def _get_image_types(self):
        """returns a (types, cimages) tuple"""

        alltypes = self.d.getVar('IMAGE_FSTYPES', True).split()
        types = []
        ctypes = self.d.getVar('COMPRESSIONTYPES', True).split()
        cimages = {}

        # Image type b depends on a having been generated first
        def addtypedepends(a, b):
            if a in alltypes:
                alltypes.remove(a)
                if b not in alltypes:
                    alltypes.append(b)
                alltypes.append(a)

        # The elf image depends on the cpio.gz image already having
        # been created, so we add that explicit ordering here.
        addtypedepends("elf", "cpio.gz")

        # jffs2 sumtool'd images need jffs2
        addtypedepends("sum.jffs2", "jffs2")

        # Filter out all the compressed images from alltypes
        for type in alltypes:
            basetype = None
            for ctype in ctypes:
                if type.endswith("." + ctype):
                    basetype = type[:-len("." + ctype)]
                    if basetype not in types:
                        types.append(basetype)
                    if basetype not in cimages:
                        cimages[basetype] = []
                    if ctype not in cimages[basetype]:
                        cimages[basetype].append(ctype)
                    break
            if not basetype and type not in types:
                types.append(type)

        # Live and VMDK images will be processed via inheriting
        # bbclass and does not get processed here.
        # vmdk depend on live images also depend on ext3 so ensure its present
        # Note: we need to ensure ext3 is in alltypes, otherwise, subimages may
        # not contain ext3 and the .rootfs.ext3 file won't be created.
        if "vmdk" in types:
            if "ext3" not in types:
                types.append("ext3")
            if "ext3" not in alltypes:
                alltypes.append("ext3")
            types.remove("vmdk")
        if "live" in types or "iso" in types or "hddimg" in types:
            if "ext3" not in types:
                types.append("ext3")
            if "ext3" not in alltypes:
                alltypes.append("ext3")
            if "live" in types:
                types.remove("live")
            if "iso" in types:
                types.remove("iso")
            if "hddimg" in types:
                types.remove("hddimg")

        return (alltypes, types, cimages)

    def _write_script(self, type, cmds):
        tempdir = self.d.getVar('T', True)
        script_name = os.path.join(tempdir, "create_image." + type)

        self.d.setVar('img_creation_func', '\n'.join(cmds))
        self.d.setVarFlag('img_creation_func', 'func', 1)
        self.d.setVarFlag('img_creation_func', 'fakeroot', 1)

        with open(script_name, "w+") as script:
            script.write("%s" % bb.build.shell_trap_code())
            script.write("export ROOTFS_SIZE=%d\n" % self._get_rootfs_size())
            bb.data.emit_func('img_creation_func', script, self.d)
            script.write("img_creation_func\n")

        os.chmod(script_name, 0775)

        return script_name

    def _get_imagecmds(self):
        old_overrides = self.d.getVar('OVERRIDES', 0)

        alltypes, types, cimages = self._get_image_types()

        image_cmds = []
        for type in types:
            cmds = []
            subimages = []

            localdata = bb.data.createCopy(self.d)
            localdata.setVar('OVERRIDES', '%s:%s' % (type, old_overrides))
            bb.data.update_data(localdata)
            localdata.setVar('type', type)

            cmds.append("\t" + localdata.getVar("IMAGE_CMD", True))
            cmds.append(localdata.expand("\tcd ${DEPLOY_DIR_IMAGE}"))

            if type in cimages:
                for ctype in cimages[type]:
                    cmds.append("\t" + localdata.getVar("COMPRESS_CMD_" + ctype, True))
                    subimages.append(type + "." + ctype)

            if type not in alltypes:
                cmds.append(localdata.expand("\trm ${IMAGE_NAME}.rootfs.${type}"))
            else:
                subimages.append(type)

            script_name = self._write_script(type, cmds)

            image_cmds.append((type, subimages, script_name))

        return image_cmds

    def create(self):
        bb.note("###### Generate images #######")
        pre_process_cmds = self.d.getVar("IMAGE_PREPROCESS_COMMAND", True)
        post_process_cmds = self.d.getVar("IMAGE_POSTPROCESS_COMMAND", True)

        execute_pre_post_process(self.d, pre_process_cmds)

        self._remove_old_symlinks()

        image_cmds = self._get_imagecmds()

        # create the images in parallel
        nproc = multiprocessing.cpu_count()
        pool = bb.utils.multiprocessingpool(nproc)
        results = list(pool.imap(generate_image, image_cmds))
        pool.close()
        pool.join()

        for result in results:
            if result is not None:
                bb.fatal(result)

        for image_type, subimages, script in image_cmds:
            bb.note("Creating symlinks for %s image ..." % image_type)
            self._create_symlinks(subimages)

        execute_pre_post_process(self.d, post_process_cmds)


def create_image(d):
    Image(d).create()

if __name__ == "__main__":
    """
    Image creation can be called independent from bitbake environment.
    """
    """
    TBD
    """
