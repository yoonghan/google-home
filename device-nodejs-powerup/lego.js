async function legoExec(motorA, motorB, hub) {
   const intervalTime = 2000

    async function setBeginning() {
       motorB.rotateByDegrees(270, 10)
       await hub.sleep(intervalTime * 2)
    }

    async function endIt() {
       motorB.rotateByDegrees(180, -10)
       await hub.sleep(intervalTime)
    }

    async function resetGear() {
       motorB.gotoRealZero(-50)
       await hub.sleep(intervalTime)
    }

    async function moveForward(timeInSeconds) {
        motorA.setPower(-100);
        await hub.sleep(timeInSeconds * 1000);
        motorA.brake()
    }

    async function moveBackward(timeInSeconds) {
        motorA.setPower(100)
        await hub.sleep(timeInSeconds * 1000)
        motorA.brake()
    }

    async function changeToSecondGear() {
        motorB.rotateByDegrees(90, -50)
        await hub.sleep(intervalTime)
    }

    async function changeToMovementGear() {
        await resetGear()
        motorB.rotateByDegrees(270, 50)
        await hub.sleep(intervalTime)
    }

    async function changeToDumpsterGear() {
        motorB.rotateByDegrees(180, -50)
        await hub.sleep(intervalTime)
    }

    async function unchangeFromDumpsterGear() {
        motorB.rotateByDegrees(180, 50)
        await hub.sleep(intervalTime)
    }

    async function start() {
        await setBeginning()
        await changeToSecondGear()
        await moveForward(5)
        await changeToDumpsterGear()
        await moveBackward(17)
        await hub.sleep(1000)
        await moveForward(13)
        await unchangeFromDumpsterGear()
        await moveBackward(5)
        await endIt()
    }

    start()
}

exports.legoExec = legoExec 
