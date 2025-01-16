<?php
$hostname = "localhost";
$database = "dbstudent";
$username = "root";
$password = "";

try {
    $db = new PDO("mysql:host=$hostname;dbname=$database", $username, $password);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $e) {
    echo json_encode(["respond" => "Database Connection Error: " . $e->getMessage()]);
    exit;
}

if (isset($_REQUEST['selectFn']) && $_REQUEST['selectFn'] == "fnSaveData") {
    $varName = trim($_REQUEST["studName"]);
    $varGender = trim($_REQUEST["studGender"]);
    $varDob = trim($_REQUEST["studDob"]);
    $varStudNo = trim($_REQUEST["studNo"]);
    $varState = trim($_REQUEST["studState"]);

    // Validate date format
    $date = DateTime::createFromFormat('Y-m-d', $varDob);
    if (!$date || $date->format('Y-m-d') !== $varDob) {
        echo json_encode(["respond" => "Invalid date format. Use YYYY-MM-DD."]);
        exit;
    }

    try {
        $stmt = $db->prepare("INSERT INTO students (stud_name, stud_no, stud_gender, stud_dob, stud_state) 
                              VALUES (:stud_name, :stud_no, :stud_gender, :stud_dob, :stud_state)");
        $stmt->execute([
            ':stud_name' => $varName,
            ':stud_no' => $varStudNo,
            ':stud_gender' => $varGender,
            ':stud_dob' => $varDob,
            ':stud_state' => $varState
        ]);

        echo json_encode(["respond" => "Information Saved!"]);
    } catch (Exception $e) {
        echo json_encode(["respond" => "Error Saving Data: " . $e->getMessage()]);
    }
} elseif (isset($_REQUEST['selectFn']) && $_REQUEST['selectFn'] == "fnSearchStud") {
    try {
        $stmt = $db->prepare('SELECT * FROM students WHERE stud_no = ?');
        $stmt->execute([$_REQUEST["studNo"]]);
        $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($rows);
    } catch (Exception $e) {
        echo json_encode(["respond" => "Error Fetching Data: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["respond" => "Invalid Function Call"]);
}
?>