import bb
import errno
import glob
import os
import shutil
import subprocess

def join(*paths):
    """Like os.path.join but doesn't treat absolute RHS specially"""
    return os.path.normpath("/".join(paths))

def relative(src, dest):
    """ Return a relative path from src to dest.

    >>> relative("/usr/bin", "/tmp/foo/bar")
    ../../tmp/foo/bar

    >>> relative("/usr/bin", "/usr/lib")
    ../lib

    >>> relative("/tmp", "/tmp/foo/bar")
    foo/bar
    """

    if hasattr(os.path, "relpath"):
        return os.path.relpath(dest, src)
    else:
        destlist = os.path.normpath(dest).split(os.path.sep)
        srclist = os.path.normpath(src).split(os.path.sep)

        # Find common section of the path
        common = os.path.commonprefix([destlist, srclist])
        commonlen = len(common)

        # Climb back to the point where they differentiate
        relpath = [ os.path.pardir ] * (len(srclist) - commonlen)
        if commonlen < len(destlist):
            # Add remaining portion
            relpath += destlist[commonlen:]

        return os.path.sep.join(relpath)

def make_relative_symlink(path):
    """ Convert an absolute symlink to a relative one """
    if not os.path.islink(path):
        return
    link = os.readlink(path)
    if not os.path.isabs(link):
        return

    # find the common ancestor directory
    ancestor = path
    depth = 0
    while ancestor and not link.startswith(ancestor):
        ancestor = ancestor.rpartition('/')[0]
        depth += 1

    if not ancestor:
        print "make_relative_symlink() Error: unable to find the common ancestor of %s and its target" % path
        return

    base = link.partition(ancestor)[2].strip('/')
    while depth > 1:
        base = "../" + base
        depth -= 1

    os.remove(path)
    os.symlink(base, path)

def format_display(path, metadata):
    """ Prepare a path for display to the user. """
    rel = relative(metadata.getVar("TOPDIR", True), path)
    if len(rel) > len(path):
        return path
    else:
        return rel

def copytree(src, dst):
    # We could use something like shutil.copytree here but it turns out to
    # to be slow. It takes twice as long copying to an empty directory. 
    # If dst already has contents performance can be 15 time slower
    # This way we also preserve hardlinks between files in the tree.

    bb.utils.mkdirhier(dst)
    cmd = 'tar -cf - -C %s -ps . | tar -xf - -C %s' % (src, dst)
    check_output(cmd, shell=True, stderr=subprocess.STDOUT)


def remove(path, recurse=True):
    """Equivalent to rm -f or rm -rf"""
    for name in glob.glob(path):
        try:
            os.unlink(name)
        except OSError, exc:
            if recurse and exc.errno == errno.EISDIR:
                shutil.rmtree(name)
            elif exc.errno != errno.ENOENT:
                raise

def symlink(source, destination, force=False):
    """Create a symbolic link"""
    try:
        if force:
            remove(destination)
        os.symlink(source, destination)
    except OSError, e:
        if e.errno != errno.EEXIST or os.readlink(destination) != source:
            raise

class CalledProcessError(Exception):
    def __init__(self, retcode, cmd, output = None):
        self.retcode = retcode
        self.cmd = cmd
        self.output = output
    def __str__(self):
        return "Command '%s' returned non-zero exit status %d with output %s" % (self.cmd, self.retcode, self.output)

# Not needed when we move to python 2.7
def check_output(*popenargs, **kwargs):
    r"""Run command with arguments and return its output as a byte string.

    If the exit code was non-zero it raises a CalledProcessError.  The
    CalledProcessError object will have the return code in the returncode
    attribute and output in the output attribute.

    The arguments are the same as for the Popen constructor.  Example:

    >>> check_output(["ls", "-l", "/dev/null"])
    'crw-rw-rw- 1 root root 1, 3 Oct 18  2007 /dev/null\n'

    The stdout argument is not allowed as it is used internally.
    To capture standard error in the result, use stderr=STDOUT.

    >>> check_output(["/bin/sh", "-c",
    ...               "ls -l non_existent_file ; exit 0"],
    ...              stderr=STDOUT)
    'ls: non_existent_file: No such file or directory\n'
    """
    if 'stdout' in kwargs:
        raise ValueError('stdout argument not allowed, it will be overridden.')
    process = subprocess.Popen(stdout=subprocess.PIPE, *popenargs, **kwargs)
    output, unused_err = process.communicate()
    retcode = process.poll()
    if retcode:
        cmd = kwargs.get("args")
        if cmd is None:
            cmd = popenargs[0]
        raise CalledProcessError(retcode, cmd, output=output)
    return output

def find(dir, **walkoptions):
    """ Given a directory, recurses into that directory,
    returning all files as absolute paths. """

    for root, dirs, files in os.walk(dir, **walkoptions):
        for file in files:
            yield os.path.join(root, file)
